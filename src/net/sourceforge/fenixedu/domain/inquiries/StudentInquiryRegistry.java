package net.sourceforge.fenixedu.domain.inquiries;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseInquiriesRegistryDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;

public class StudentInquiryRegistry extends StudentInquiryRegistry_Base {

	public StudentInquiryRegistry() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public StudentInquiryRegistry(ExecutionCourse executionCourse, ExecutionSemester executionPeriod,
			CurricularCourse curricularCourse, Registration registration) {
		this();
		setExecutionCourse(executionCourse);
		setExecutionPeriod(executionPeriod);
		setCurricularCourse(curricularCourse);
		setRegistration(registration);
		setStudent(registration.getStudent());

		setState(executionCourse.getAvailableForInquiries() ? InquiriesRegistryState.ANSWER_LATER : InquiriesRegistryState.UNAVAILABLE);
		if (curricularCourse.isAnual()) {
			ExecutionSemester previousExecutionPeriod = executionPeriod.getPreviousExecutionPeriod();
			if (previousExecutionPeriod.getExecutionYear() != executionPeriod.getExecutionYear()) {
				setState(InquiriesRegistryState.UNAVAILABLE);
			}
		}
	}

	public boolean isAnswered() {
		return getState() == InquiriesRegistryState.ANSWERED;
	}

	public boolean isNotAnswered() {
		return getState() == InquiriesRegistryState.NOT_ANSWERED;
	}

	public boolean isToAnswerLater() {
		return isAvailableToInquiries() && getState() == InquiriesRegistryState.ANSWER_LATER;
	}

	public boolean isUnavailable() {
		return getState() == InquiriesRegistryState.UNAVAILABLE;
	}

	public boolean isToAnswerTeachers() {
		return getState() == InquiriesRegistryState.TEACHERS_TO_ANSWER;
	}

	public boolean isAvailableToInquiries() {
		return getExecutionCourse().getAvailableForInquiries() && !isUnavailable();
	}

	public boolean isOpenToAnswer() {

		if (isAnswered() || isNotAnswered()) {
			return false;
		}
		if (isCreatedAfterWeeklySpentHoursSubmission()) {
			return false;
		}
		if (getRegistration().getStudent().isWeeklySpentHoursSubmittedForOpenInquiry() && !isAvailableToInquiries()) {
			return false;
		}
		return true;
	}

	public InquiryGradesInterval getLastGradeInterval() {
		Collection<Enrolment> enrolments = getRegistration().getEnrolments(getExecutionPeriod());
		Grade grade = null;
		for (Enrolment enrolment : enrolments) {
			if (getExecutionCourse() == enrolment.getExecutionCourseFor(getExecutionPeriod())) {
				final EnrolmentEvaluation enrolmentEvaluation = enrolment.getLatestEnrolmentEvaluation();
				if (enrolmentEvaluation != null && (enrolmentEvaluation.isTemporary() || enrolmentEvaluation.isFinal())) {
					grade = enrolmentEvaluation.getGrade();
					break;
				}
			}
		}
		if (grade != null && grade.getGradeScale() == GradeScale.TYPE20) {
			int gradeValue = 0;
			if (grade.isApproved()) {
				gradeValue = grade.getIntegerValue();
			}
			return InquiryGradesInterval.getInterval(Double.valueOf(gradeValue) * 10);
		}
		return null;
	}

	public boolean isCreatedAfterWeeklySpentHoursSubmission() {
		return getRegistration().getStudent().isWeeklySpentHoursSubmittedForOpenInquiry()
				&& (getWeeklyHoursSpentPercentage() == null || getStudyDaysSpentInExamsSeason() == null);
	}

	public StudentInquiryExecutionPeriod getStudentInquiryExecutionPeriod() {
		return getRegistration().getStudent().getStudentInquiryExecutionPeriod(getExecutionPeriod());
	}

	public ExecutionDegree getExecutionDegree() {
		final StudentCurricularPlan studentCurricularPlan = getRegistration().getActiveStudentCurricularPlan();
		if (studentCurricularPlan != null) {
			final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
			return degreeCurricularPlan.getExecutionDegreeByAcademicInterval(getExecutionPeriod().getAcademicInterval());
		}
		DegreeCurricularPlan lastDegreeCurricularPlan = getRegistration().getLastDegreeCurricularPlan();
		if (lastDegreeCurricularPlan != null) {
			return lastDegreeCurricularPlan.getExecutionDegreeByAcademicInterval(getExecutionPeriod().getAcademicInterval());
		}
		return null;
	}

	public static boolean checkTotalPercentageDistribution(List<CurricularCourseInquiriesRegistryDTO> courses) {
		Integer totalPercentage = 0;
		for (CurricularCourseInquiriesRegistryDTO curricularCourseInquiriesRegistryDTO : courses) {
			totalPercentage += curricularCourseInquiriesRegistryDTO.getWeeklyHoursSpentPercentage();
		}
		return totalPercentage == 100;
	}

	public static boolean checkTotalStudyDaysSpentInExamsSeason(List<CurricularCourseInquiriesRegistryDTO> courses) {
		int totalHours = 0;
		for (CurricularCourseInquiriesRegistryDTO curricularCourseInquiriesRegistryDTO : courses) {
			totalHours += curricularCourseInquiriesRegistryDTO.getStudyDaysSpentInExamsSeason();
		}
		return totalHours <= 60 * 24 * 8; //60 days in exam period 
	}

	public void delete() {
		removeCurricularCourse();
		removeExecutionCourse();
		removeExecutionPeriod();
		removeRegistration();
		removeStudent();
		removeRootDomainObject();
		super.deleteDomainObject();
	}
}
