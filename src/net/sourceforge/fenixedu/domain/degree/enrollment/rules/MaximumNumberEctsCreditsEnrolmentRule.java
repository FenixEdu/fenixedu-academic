package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

public class MaximumNumberEctsCreditsEnrolmentRule implements IEnrollmentRule {

    private StudentCurricularPlan studentCurricularPlan;
    private ExecutionPeriod executionPeriod;
    
    public MaximumNumberEctsCreditsEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {
        this.studentCurricularPlan = studentCurricularPlan;
        this.executionPeriod = executionPeriod;
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {
        List allStudentEnrolledEnrollments = this.studentCurricularPlan
        .getAllStudentEnrolledEnrollmentsInExecutionPeriod(this.executionPeriod);

        Double maxECTS = 40.0;
        Double ects = 0.0;
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

}
