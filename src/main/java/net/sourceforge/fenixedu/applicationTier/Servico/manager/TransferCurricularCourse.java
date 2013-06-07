package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class TransferCurricularCourse {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Deprecated
    @Service
    public static void run(String sourceExecutionCourseId, final String curricularCourseId, String destinationExecutionCourseId) {

        if (StringUtils.isEmpty(destinationExecutionCourseId) || !StringUtils.isNumeric(destinationExecutionCourseId)) {
            throw new DomainException("error.selection.noDestinationExecutionCourse");
        }

        if (destinationExecutionCourseId.equals(sourceExecutionCourseId)) {
            throw new DomainException("error.selection.sameSourceDestinationCourse");
        }

        final ExecutionCourse sourceExecutionCourse = AbstractDomainObject.fromExternalId(sourceExecutionCourseId);
        final ExecutionCourse destinationExecutionCourse = AbstractDomainObject.fromExternalId(destinationExecutionCourseId);

        if (destinationExecutionCourse == null) {
            throw new DomainException("error.selection.noDestinationExecutionCourse");
        }

        CurricularCourse curricularCourse = null;
        for (final CurricularCourse curricularCourseOther : sourceExecutionCourse.getAssociatedCurricularCoursesSet()) {
            if (curricularCourseOther.getExternalId().equals(curricularCourseId)) {
                curricularCourse = curricularCourseOther;
                break;
            }
        }

        if (curricularCourse == null) {
            curricularCourse = AbstractDomainObject.fromExternalId(curricularCourseId);

            StringBuilder curricularCourseNameSB = new StringBuilder();
            if (StringUtils.isEmpty(curricularCourse.getNameI18N().getContent())) {
                curricularCourseNameSB.append(curricularCourse.getName());
            } else {
                curricularCourseNameSB.append(curricularCourse.getNameI18N().getContent());
            }
            curricularCourseNameSB.append(" [" + curricularCourse.getDegree().getSigla() + "]");

            String sourceExecutionCourseName = sourceExecutionCourse.getNameI18N().getContent();
            if (StringUtils.isEmpty(sourceExecutionCourseName)) {
                sourceExecutionCourseName = sourceExecutionCourse.getName();
            }

            throw new DomainException("error.manager.executionCourseManagement.transferCurricularCourse.gone",
                    curricularCourseNameSB.toString(), sourceExecutionCourseName);
        }

        deleteShiftStudents(sourceExecutionCourse, curricularCourse);

        Set<String> transferedStudents = new HashSet<String>();
        transferAttends(destinationExecutionCourseId, sourceExecutionCourse, destinationExecutionCourse, curricularCourse,
                transferedStudents);

        sourceExecutionCourse.removeAssociatedCurricularCourses(curricularCourse);
        destinationExecutionCourse.addAssociatedCurricularCourses(curricularCourse);
    }

    /**
     * @param destinationExecutionCourseId
     * @param sourceExecutionCourse
     * @param destinationExecutionCourse
     * @param curricularCourse
     * @param transferedStudents
     * @throws ExcepcaoPersistencia
     */
    private static void transferAttends(String destinationExecutionCourseId, ExecutionCourse sourceExecutionCourse,
            ExecutionCourse destinationExecutionCourse, CurricularCourse curricularCourse, Set<String> transferedStudents) {
        for (Attends attend : sourceExecutionCourse.getAttends()) {
            Enrolment enrollment = attend.getEnrolment();
            final Registration registration = attend.getRegistration();
            if (enrollment != null) {
                CurricularCourse associatedCurricularCourse = attend.getEnrolment().getCurricularCourse();
                if (curricularCourse == associatedCurricularCourse) {
                    Attends existingAttend =
                            (Attends) CollectionUtils.find(destinationExecutionCourse.getAttends(), new Predicate() {
                                @Override
                                public boolean evaluate(Object arg0) {
                                    Attends attendFromDestination = (Attends) arg0;
                                    return (attendFromDestination.getRegistration() == registration);
                                }
                            });
                    if (existingAttend != null) {
                        existingAttend.setEnrolment(enrollment);
                        attend.delete();
                    } else {
                        attend.setDisciplinaExecucao(destinationExecutionCourse);
                    }

                    transferedStudents.add(registration.getExternalId());
                }
            }
        }
    }

    /**
     * @param sourceExecutionCourse
     * @param curricularCourse
     * @throws ExcepcaoPersistencia
     */
    private static void deleteShiftStudents(ExecutionCourse sourceExecutionCourse, CurricularCourse curricularCourse) {

        Set<Shift> shifts = sourceExecutionCourse.getAssociatedShifts();

        for (Shift shift : shifts) {
            Iterator<Registration> iter = shift.getStudentsIterator();
            while (iter.hasNext()) {
                Registration registration = iter.next();
                Attends attend = sourceExecutionCourse.getAttendsByStudent(registration);

                if (attend.getEnrolment() != null && attend.getEnrolment().getCurricularCourse() == curricularCourse) {
                    iter.remove();
                }
            }
        }
    }

}