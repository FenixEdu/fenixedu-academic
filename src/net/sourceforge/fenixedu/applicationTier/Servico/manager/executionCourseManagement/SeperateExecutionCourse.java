package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.utils.ExecutionCourseUtils;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class SeperateExecutionCourse extends Service {

    public void run(final Integer originExecutionCourseOid, final Integer destinationExecutionCourseId,
            final Integer[] shiftIdsToTransfer, final Integer[] curricularCourseIdsToTransfer)
            throws ExcepcaoPersistencia {

        final ExecutionCourse originExecutionCourse = rootDomainObject.readExecutionCourseByOID( originExecutionCourseOid);
        ExecutionCourse destinationExecutionCourse = rootDomainObject.readExecutionCourseByOID( destinationExecutionCourseId);
        if (destinationExecutionCourse == null) {
            destinationExecutionCourse = createNewExecutionCourse(originExecutionCourse);
            destinationExecutionCourse.createSite();
            ExecutionCourseUtils.copyBibliographicReference(originExecutionCourse, destinationExecutionCourse);
            ExecutionCourseUtils.copyEvaluationMethod(originExecutionCourse, destinationExecutionCourse);           
        }

        transferCurricularCourses(originExecutionCourse, destinationExecutionCourse, curricularCourseIdsToTransfer);

        transferAttends(originExecutionCourse, destinationExecutionCourse);

        transferShifts(originExecutionCourse, destinationExecutionCourse, shiftIdsToTransfer);

        fixStudentShiftEnrolements(originExecutionCourse);
        fixStudentShiftEnrolements(destinationExecutionCourse);

        associateGroupings(originExecutionCourse, destinationExecutionCourse);
    }

    private void transferCurricularCourses(final ExecutionCourse originExecutionCourse, final ExecutionCourse destinationExecutionCourse, 
            final Integer[] curricularCourseIdsToTransfer) {
        for (final Integer curricularCourseID : curricularCourseIdsToTransfer) {
            final CurricularCourse curricularCourse = (CurricularCourse) findDomainObjectByID(
                    originExecutionCourse.getAssociatedCurricularCourses(), curricularCourseID);
            destinationExecutionCourse.addAssociatedCurricularCourses(curricularCourse);
            originExecutionCourse.removeAssociatedCurricularCourses(curricularCourse);
        }
    }

    private void transferAttends(final ExecutionCourse originExecutionCourse, final ExecutionCourse destinationExecutionCourse) {
        final List<CurricularCourse> curricularCourses = destinationExecutionCourse.getAssociatedCurricularCourses();
        for (int i = 0; i < originExecutionCourse.getAttends().size(); i++) {
            final Attends attends = originExecutionCourse.getAttends().get(i);
            final Enrolment enrolment = attends.getEnrolment();
            if (enrolment != null && curricularCourses.contains(enrolment.getCurricularCourse())) {
                attends.setDisciplinaExecucao(destinationExecutionCourse);
                i--;
            }
        }
    }

    private void transferShifts(final ExecutionCourse originExecutionCourse, final ExecutionCourse destinationExecutionCourse, 
            final Integer[] shiftIdsToTransfer) {
        for (final Integer shiftId : shiftIdsToTransfer) {
            final Shift shift = (Shift) findDomainObjectByID(originExecutionCourse.getAssociatedShifts(), shiftId);
            shift.setDisciplinaExecucao(destinationExecutionCourse);
        }
    }

    private DomainObject findDomainObjectByID(final List domainObjects, final Integer id) {
        for (final DomainObject domainObject : (List<DomainObject>) domainObjects) {
            if (domainObject.getIdInternal().equals(id)) {
                return domainObject;
            }
        }
        return null;
    }

    private void fixStudentShiftEnrolements(final ExecutionCourse executionCourse) {
        for (final Shift shift : executionCourse.getAssociatedShifts()) {
            for (int i = 0; i < shift.getStudents().size(); i++) {
                final Registration registration = shift.getStudents().get(i);
                if (!registration.attends(executionCourse)) {
                    shift.removeStudents(registration);
                }
            }
        }
    }

    private void associateGroupings(final ExecutionCourse originExecutionCourse, final ExecutionCourse destinationExecutionCourse) {
        for (final Grouping grouping : originExecutionCourse.getGroupings()) {
            for (final StudentGroup studentGroup : grouping.getStudentGroups()) {
                studentGroup.getAttends().clear();
                studentGroup.delete();
            }
            grouping.delete();
        }
    }

    private ExecutionCourse createNewExecutionCourse(ExecutionCourse originExecutionCourse) throws ExcepcaoPersistencia {
        ExecutionCourse destinationExecutionCourse = new ExecutionCourse();
        destinationExecutionCourse.setComment("");
        destinationExecutionCourse.setExecutionPeriod(originExecutionCourse.getExecutionPeriod());
        destinationExecutionCourse.setLabHours(originExecutionCourse.getLabHours());
        destinationExecutionCourse.setNome(originExecutionCourse.getNome());
        destinationExecutionCourse.createForum(originExecutionCourse.getNome(), originExecutionCourse.getNome());
        destinationExecutionCourse.setPraticalHours(originExecutionCourse.getPraticalHours());
        destinationExecutionCourse.setSigla(originExecutionCourse.getSigla() + System.currentTimeMillis());

        for (int i = 0; i < originExecutionCourse.getProfessorships().size(); i++) {
            Professorship professorship = originExecutionCourse.getProfessorships().get(i);
            Professorship newProfessorship = new Professorship();
            newProfessorship.setExecutionCourse(destinationExecutionCourse);
            newProfessorship.setTeacher(professorship.getTeacher());
            newProfessorship.setResponsibleFor(professorship.getResponsibleFor());
            destinationExecutionCourse.getProfessorships().add(newProfessorship);
        }      

        destinationExecutionCourse.setSigla(getUniqueExecutionCourseCode(
                originExecutionCourse.getNome(), originExecutionCourse.getExecutionPeriod(),
                originExecutionCourse.getSigla()));
        destinationExecutionCourse.setTheoPratHours(originExecutionCourse.getTheoPratHours());
        destinationExecutionCourse.setTheoreticalHours(originExecutionCourse.getTheoreticalHours());
        return destinationExecutionCourse;
    }

    private String getUniqueExecutionCourseCode(final String executionCourseName,
            final ExecutionPeriod executionPeriod, final String originalExecutionCourseCode)
            throws ExcepcaoPersistencia {
        Set executionCourseCodes = getExecutionCourseCodes(executionPeriod);

        StringBuilder executionCourseCode = new StringBuilder(originalExecutionCourseCode);
        executionCourseCode.append("-1");
        int startVariablePartIndex = executionCourseCode.length() - 1;
        String destinationExecutionCourseCode = executionCourseCode.toString();
        for (int i = 1; executionCourseCodes.contains(destinationExecutionCourseCode); ++i) {
            executionCourseCode.replace(startVariablePartIndex, executionCourseCode.length(), "");
            executionCourseCode.append(i);
            destinationExecutionCourseCode = executionCourseCode.toString();
        }

        return destinationExecutionCourseCode;
    }

    private Set getExecutionCourseCodes(ExecutionPeriod executionPeriod) throws ExcepcaoPersistencia {
        List executionCourses = executionPeriod.getAssociatedExecutionCourses();
        return new HashSet(CollectionUtils.collect(executionCourses, new Transformer() {
            public Object transform(Object arg0) {
                ExecutionCourse executionCourse = (ExecutionCourse) arg0;
                return executionCourse.getSigla().toUpperCase();
            }
        }));
    }

    boolean contains(Integer[] integerArray, Integer integer) {
        for (int i = 0; i < integerArray.length; i++) {
            if (integer.equals(integerArray[i])) {
                return true;
            }
        }

        return false;
    }

}