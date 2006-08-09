/**
 * 
 */


package net.sourceforge.fenixedu.applicationTier.Servico.teacher;


import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.AttendacyStateSelectionType;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 13:44:51,27/Mar/2006 <br/> This is a version of
 *         <code>ReadStudentsWithAttendsByExecutionCourse</code> that returns
 *         domain objects instead of the ugly
 *         <code>TeacherSiteAdministrationView</code>/<code>component</code>
 *         object
 * @version $Id$
 */
public class ReadDomainStudentsByExecutionCourseAndDegreeTypeAndShiftAttendAndEnrollmentType extends
		Service {

	private class CurricularPlanIdPredicate implements Predicate {

		private Collection<Integer> curricularPlanIds;

		private CurricularPlanIdPredicate(Collection<Integer> curricularPlanIds) {
			this.curricularPlanIds = curricularPlanIds;
		}

		public boolean evaluate(Object arg0) {
			Attends attends = (Attends) arg0;
			StudentCurricularPlan activeSCP = attends.getAluno().getActiveStudentCurricularPlan();
			return activeSCP!=null && (this.curricularPlanIds == null
					|| this.curricularPlanIds.contains(activeSCP.getDegreeCurricularPlan().getIdInternal()));
		}
	}

	private class ShiftIdPredicate implements Predicate {

		private Collection<Integer> shiftIds;

		private ShiftIdPredicate(Collection<Integer> shiftIds) {
			this.shiftIds = shiftIds;
		}

		public boolean evaluate(Object arg0) {
			Attends attends = (Attends) arg0;
			if (shiftIds == null) return true;
			else {
				Student student = attends.getAluno();
				for (Integer shiftId : shiftIds) {
					for (Shift shift : student.getShifts()) {
						if (shift.getIdInternal().equals(shiftId)) {
							return true;
						}
					}
				}

			}
			return false;
		}
	}
	
	private class AttendsStudentTransformer implements Transformer
	{

		public Object transform(Object arg0) {
			return ((Attends)arg0).getAluno();
		}
		
	}

	private class EnrollmentTypeFilter implements Predicate {

		private Collection<AttendacyStateSelectionType> enrollmentTypes;

		private ExecutionCourse executionCourse;

		private EnrollmentTypeFilter(ExecutionCourse executionCourse,
				Collection<AttendacyStateSelectionType> enrollmentTypes) {
			this.enrollmentTypes = enrollmentTypes;
			this.executionCourse = executionCourse;
		}

		public boolean evaluate(Object arg0) {
			boolean result = false;
			Attends attends = (Attends) arg0;
			Enrolment enrollment = attends.getEnrolment();
			// i am sorry if this else/if statements burn your eyes, but i think
			// its easier to read than a single if statement with multiple
			// disjunctions
			if (this.enrollmentTypes == null) {
				result = true;
			}
			else if (this.enrollmentTypes.contains(AttendacyStateSelectionType.NOT_ENROLLED)
					&& enrollment == null) {
				result = true;
			}
			else if (enrollment != null && this.enrollmentTypes.contains(AttendacyStateSelectionType.IMPROVEMENT)
					&& enrollment.isImprovementForExecutionCourse(executionCourse)) {
				result = true;
			}
			else if (enrollment != null &&  this.enrollmentTypes.contains(AttendacyStateSelectionType.ENROLLED)
					&& (!enrollment.isImprovementForExecutionCourse(executionCourse) && enrollment.getEnrollmentState().equals(EnrollmentState.ENROLLED))
					&& enrollment.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.NORMAL)) {
				result = true;
			}
			else if (enrollment != null &&  this.enrollmentTypes.contains(AttendacyStateSelectionType.SPECIAL_SEASON)
					&& enrollment.isSpecialSeason()) {
				result = true;
			}

			return result;
		}
	}

	public Collection<Student> run(Integer executionCourseId, List<Integer> curricularPlansIds,
			List<AttendacyStateSelectionType> enrollmentTypeFilters, List<Integer> shiftIds)
			throws FenixServiceException, ExcepcaoPersistencia {

		ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
		List<Attends> attends = executionCourse.getAttends();
		Collection<Enrolment> enrollmentsInWantedState = CollectionUtils.select(attends, new EnrollmentTypeFilter(executionCourse, enrollmentTypeFilters));
		Collection<Enrolment> enrollmentsInWantedDegree = CollectionUtils.select(enrollmentsInWantedState, new CurricularPlanIdPredicate(curricularPlansIds));
		Collection<Enrolment> enrollmentsInWantedShifts = CollectionUtils.select(enrollmentsInWantedDegree, new ShiftIdPredicate(shiftIds));
		

		return CollectionUtils.collect(enrollmentsInWantedShifts,new AttendsStudentTransformer());
	}

}
