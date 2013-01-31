package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class InquiryCourseAnswer extends InquiryCourseAnswer_Base {

	public InquiryCourseAnswer() {
		super();
	}

	public InquiryCourseAnswer(StudentInquiryRegistry inquiryRegistry) {
		this();
		setExecutionPeriod(inquiryRegistry.getExecutionPeriod());
		setExecutionCourse(inquiryRegistry.getExecutionCourse());
		setExecutionDegreeStudent(inquiryRegistry.getExecutionDegree());

		setWeeklyHoursSpentPercentage(inquiryRegistry.getWeeklyHoursSpentPercentage());
		setStudyDaysSpentInExamsSeason(inquiryRegistry.getStudyDaysSpentInExamsSeason());
		setAttendenceClassesPercentage(inquiryRegistry.getAttendenceClassesPercentage());

		setExecutionDegreeCourse(ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(inquiryRegistry.getCurricularCourse()
				.getDegreeCurricularPlan(), inquiryRegistry.getExecutionPeriod().getExecutionYear()));
	}

	@Checked("RolePredicates.STUDENT_PREDICATE")
	@Service
	public static InquiryCourseAnswer createNotAnsweredInquiryCourse(final StudentInquiryRegistry inquiryRegistry,
			final InquiryNotAnsweredJustification justification, final String otherJustification) {
		final InquiryCourseAnswer courseAnswer = new InquiryCourseAnswer(inquiryRegistry);
		final StudentInquiryExecutionPeriod studentInquiryExecutionPeriod =
				inquiryRegistry.getRegistration().getStudent()
						.getStudentInquiryExecutionPeriod(inquiryRegistry.getExecutionPeriod());
		courseAnswer.setWeeklyHoursSpentInAutonomousWork(studentInquiryExecutionPeriod.getWeeklyHoursSpentInClassesSeason());
		courseAnswer.setNotAnsweredJustification(justification);
		courseAnswer.setNotAnsweredOtherJustification(otherJustification);
		courseAnswer.setResponseDateTime(new DateTime());

		courseAnswer.setNumberOfEnrolments(getNumberOfEnrolments(inquiryRegistry));
		courseAnswer.setCommittedFraud(Boolean.FALSE);//TODO actualmente não existe registo desta info no fenix
		courseAnswer.setStudentType(inquiryRegistry.getRegistration().getRegistrationAgreement());
		courseAnswer.setEntryGrade(InquiryGradesInterval.getInterval(inquiryRegistry.getRegistration().getEntryGrade()));
		courseAnswer.setGrade(inquiryRegistry.getLastGradeInterval());

		inquiryRegistry.setState(InquiriesRegistryState.NOT_ANSWERED);

		return courseAnswer;
	}

	public static int getNumberOfEnrolments(final StudentInquiryRegistry inquiryRegistry) {
		final StudentCurricularPlan studentCurricularPlan =
				inquiryRegistry.getRegistration().getStudentCurricularPlan(
						inquiryRegistry.getExecutionPeriod().getExecutionYear());
		final int numberOfEnrolments = studentCurricularPlan.getEnrolments(inquiryRegistry.getCurricularCourse()).size();
		return numberOfEnrolments;
	}
}
