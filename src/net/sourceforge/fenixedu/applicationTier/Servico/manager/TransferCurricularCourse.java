package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class TransferCurricularCourse extends Service {

    public void run(Integer sourceExecutionCourseId, final Integer curricularCourseId,
            Integer destinationExecutionCourseId) throws ExcepcaoPersistencia {

        final ExecutionCourse sourceExecutionCourse = rootDomainObject.readExecutionCourseByOID(sourceExecutionCourseId);
        final ExecutionCourse destinationExecutionCourse = rootDomainObject.readExecutionCourseByOID(destinationExecutionCourseId);

        CurricularCourse curricularCourse = null;
        for (final CurricularCourse curricularCourseOther : sourceExecutionCourse.getAssociatedCurricularCoursesSet()) {
            if (curricularCourseOther.getIdInternal().equals(curricularCourseId)) {
                curricularCourse = curricularCourseOther;
                break;
            }
        }

        deleteShiftStudents(sourceExecutionCourse, curricularCourse);
        
        Set<Integer> transferedStudents = new HashSet<Integer>();
        transferAttends(destinationExecutionCourseId, sourceExecutionCourse, destinationExecutionCourse,
                curricularCourse, transferedStudents);

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
    private void transferAttends(Integer destinationExecutionCourseId,
            ExecutionCourse sourceExecutionCourse, ExecutionCourse destinationExecutionCourse,
            CurricularCourse curricularCourse, Set<Integer> transferedStudents)
            throws ExcepcaoPersistencia {
        for (Attends attend : sourceExecutionCourse.getAttends()) {
            Enrolment enrollment = attend.getEnrolment();
            final Registration registration = attend.getRegistration();
            if (enrollment != null) {
                CurricularCourse associatedCurricularCourse = attend.getEnrolment()
                        .getCurricularCourse();
                if (curricularCourse == associatedCurricularCourse) {
                    Attends existingAttend = (Attends) CollectionUtils.find(destinationExecutionCourse
                            .getAttends(), new Predicate() {
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

                    transferedStudents.add(registration.getIdInternal());
                }
            }
        }
    }

    /**
     * @param sourceExecutionCourse
     * @param curricularCourse
     * @throws ExcepcaoPersistencia
     */
    private void deleteShiftStudents(ExecutionCourse sourceExecutionCourse, CurricularCourse curricularCourse)
            throws ExcepcaoPersistencia {
        List<Shift> shifts = sourceExecutionCourse.getAssociatedShifts();

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
