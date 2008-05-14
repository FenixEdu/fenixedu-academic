package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

public class MaximumNumberEctsCreditsEnrolmentRule implements IEnrollmentRule {

    private StudentCurricularPlan studentCurricularPlan;
    private ExecutionSemester executionSemester;
    
    public MaximumNumberEctsCreditsEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
            ExecutionSemester executionSemester) {
        this.studentCurricularPlan = studentCurricularPlan;
        this.executionSemester = executionSemester;
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {
        List allStudentEnrolledEnrollments = this.studentCurricularPlan
        .getAllStudentEnrolledEnrollmentsInExecutionPeriod(this.executionSemester);

        Double maxECTS = 40.0;
        Double ects = getAnualCurricularCoursesECTS(curricularCoursesToBeEnrolledIn);
        for (Enrolment enrolment : (List<Enrolment>) allStudentEnrolledEnrollments) {
	    ects += enrolment.getAccumulatedEctsCredits();
	}
        
        Double availableECTS = ((maxECTS - ects) > 0) ? maxECTS - ects : 0.0;  
        
        Set<CurricularCourse2Enroll> curricularCourse2Remove = new HashSet<CurricularCourse2Enroll>();
        for (CurricularCourse2Enroll curricularCourse2Enroll : curricularCoursesToBeEnrolledIn) {
	    if(curricularCourse2Enroll.getEctsCredits() > availableECTS) {
		curricularCourse2Remove.add(curricularCourse2Enroll);
	    }
	}
        
        curricularCoursesToBeEnrolledIn.removeAll(curricularCourse2Remove);
	return curricularCoursesToBeEnrolledIn;
    }
    
    private Double getAnualCurricularCoursesECTS(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	Double ects = 0.0;
	for (Enrolment enrolment : studentCurricularPlan.getEnrolmentsByExecutionPeriod(executionSemester.getPreviousExecutionPeriod())) {
	    if(enrolment.getCurricularCourse().isAnual()) {
		ects += enrolment.getCurricularCourse().getEctsCredits();
		removeAnualCourseFromCoursesToEnroll(enrolment.getCurricularCourse(), curricularCoursesToBeEnrolledIn);
	    }
	}
	return ects;
    }

    private void removeAnualCourseFromCoursesToEnroll(CurricularCourse curricularCourse, List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	for (CurricularCourse2Enroll curricularCourse2Enroll : curricularCoursesToBeEnrolledIn) {
	    if(curricularCourse2Enroll.getCurricularCourse().equals(curricularCourse)) {
		curricularCoursesToBeEnrolledIn.remove(curricularCourse2Enroll);
		return;
	    }
	}
    }

}
