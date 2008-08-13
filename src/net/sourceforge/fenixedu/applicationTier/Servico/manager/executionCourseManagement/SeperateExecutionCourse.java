package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateExecutionCoursesForDegreeCurricularPlansAndExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.utils.ExecutionCourseUtils;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
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
            {

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
                    originExecutionCourse.getAssociatedCurricularCoursesSet(), curricularCourseID);
            originExecutionCourse.removeAssociatedCurricularCourses(curricularCourse);
            destinationExecutionCourse.addAssociatedCurricularCourses(curricularCourse);
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

    private void transferShifts(final ExecutionCourse originExecutionCourse, final ExecutionCourse destinationExecutionCourse, final Integer[] shiftIdsToTransfer) {
        for (final Integer shiftId : shiftIdsToTransfer) {
            final Shift shift = (Shift) findDomainObjectByID(originExecutionCourse.getAssociatedShifts(), shiftId);            
            List<CourseLoad> courseLoads = shift.getCourseLoads();
            for (Iterator<CourseLoad> iter = courseLoads.iterator(); iter.hasNext();) {
		CourseLoad courseLoad = iter.next();
		CourseLoad newCourseLoad = destinationExecutionCourse.getCourseLoadByShiftType(courseLoad.getType());
		if(newCourseLoad == null) {
		    newCourseLoad = new CourseLoad(destinationExecutionCourse, courseLoad.getType(), courseLoad.getUnitQuantity(),
			    courseLoad.getTotalQuantity());
		}
		iter.remove();		
		shift.removeCourseLoads(courseLoad);
		shift.addCourseLoads(newCourseLoad);
	    }
            
        }
    }

    private DomainObject findDomainObjectByID(final Set<? extends DomainObject> domainObjects, final Integer id) {
        for (final DomainObject domainObject : (Set<DomainObject>) domainObjects) {
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

    private ExecutionCourse createNewExecutionCourse(ExecutionCourse originExecutionCourse) {
	final String sigla = getUniqueExecutionCourseCode(originExecutionCourse.getNome(),
		originExecutionCourse.getExecutionPeriod(), originExecutionCourse.getSigla());

	final ExecutionCourse destinationExecutionCourse = new ExecutionCourse(originExecutionCourse
		.getNome(), sigla, originExecutionCourse.getExecutionPeriod(), null);

	for (CourseLoad courseLoad : originExecutionCourse.getCourseLoads()) {
	    new CourseLoad(destinationExecutionCourse, courseLoad.getType(), courseLoad.getUnitQuantity(), 
		    courseLoad.getTotalQuantity());
	}
	
	for (final Professorship professorship : originExecutionCourse.getProfessorships()) {
	    final Professorship newProfessorship = new Professorship();
	    newProfessorship.setExecutionCourse(destinationExecutionCourse);
	    newProfessorship.setTeacher(professorship.getTeacher());
	    newProfessorship.setResponsibleFor(professorship.getResponsibleFor());

	    destinationExecutionCourse.getProfessorships().add(newProfessorship);
	}

	return destinationExecutionCourse;
    }

    private String getUniqueExecutionCourseCode(final String executionCourseName,
            final ExecutionSemester executionSemester, final String originalExecutionCourseCode) {
	Set<String> executionCourseCodes = getExecutionCourseCodes(executionSemester);
	return CreateExecutionCoursesForDegreeCurricularPlansAndExecutionPeriod.getUniqueSigla(executionCourseCodes, originalExecutionCourseCode);
//
//        StringBuilder executionCourseCode = new StringBuilder(originalExecutionCourseCode);
//        executionCourseCode.append("-1");
//        int startVariablePartIndex = executionCourseCode.length() - 1;
//        String destinationExecutionCourseCode = executionCourseCode.toString();
//        for (int i = 1; executionCourseCodes.contains(destinationExecutionCourseCode); ++i) {
//            executionCourseCode.replace(startVariablePartIndex, executionCourseCode.length(), "");
//            executionCourseCode.append(i);
//            destinationExecutionCourseCode = executionCourseCode.toString();
//        }
//
//        return destinationExecutionCourseCode;
    }

    private Set<String> getExecutionCourseCodes(ExecutionSemester executionSemester) {
        List<ExecutionCourse> executionCourses = executionSemester.getAssociatedExecutionCourses();
        return new HashSet<String>(CollectionUtils.collect(executionCourses, new Transformer() {
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