package net.sourceforge.fenixedu.domain.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.CurricularCourseInquiriesRegistryDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
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
	setStudent(registration);
	setState(executionCourse.getAvailableForInquiries() ? InquiriesRegistryState.ANSWER_LATER
		: InquiriesRegistryState.UNAVAILABLE);
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

    public boolean isAvailableToInquiries() {
	return getExecutionCourse().getAvailableForInquiries();
    }

    public StudentInquiryExecutionPeriod getStudentInquiryExecutionPeriod() {
	return getStudent().getStudent().getStudentInquiryExecutionPeriod(getExecutionPeriod());
    }

    public ExecutionDegree getExecutionDegree() {
	final StudentCurricularPlan studentCurricularPlan = getStudent().getActiveStudentCurricularPlan();
	if (studentCurricularPlan != null) {
	    final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
	    return degreeCurricularPlan.getExecutionDegreeByAcademicInterval(getExecutionPeriod().getAcademicInterval());
	}
	DegreeCurricularPlan lastDegreeCurricularPlan = getStudent().getLastDegreeCurricularPlan();
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
}
