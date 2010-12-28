package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
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
	//	final StudentInquiryExecutionPeriod studentInquiryExecutionPeriod = inquiryRegistry.getStudent().getStudent()
	//		.getStudentInquiryExecutionPeriod(inquiryRegistry.getExecutionPeriod());
	//courseAnswer.setWeeklyHoursSpentInClassesSeason(studentInquiryExecutionPeriod.getWeeklyHoursSpentInClassesSeason());
	courseAnswer.setNotAnsweredJustification(justification);
	courseAnswer.setNotAnsweredOtherJustification(otherJustification);

	inquiryRegistry.setState(InquiriesRegistryState.NOT_ANSWERED);

	return courseAnswer;
    }
}
