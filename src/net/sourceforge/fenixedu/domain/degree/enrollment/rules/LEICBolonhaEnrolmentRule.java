package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

public class LEICBolonhaEnrolmentRule extends BolonhaEnrolmentRule {
    
    private static final String DISSERTACAO = "BAI";
    
    private static final String INVESTIGACAO = "B7N";
    
    private static final String TFCI = "B63";
    
    private static final String TFCII = "B64";

    private static final String[] DEGREE = {TFCI, TFCII};

    private static final String[] MASTER_DEGREE = {DISSERTACAO, INVESTIGACAO};
    
    public LEICBolonhaEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionPeriod executionPeriod) {
	super(studentCurricularPlan, executionPeriod);
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {

	if(studentCurricularPlan.getBranch() != null && studentCurricularPlan.getSecundaryBranch() != null) {
	    
	    if(isEnrolledInExecutionPeriod(DISSERTACAO) || isEnrolledInPreviousExecutionPeriod(DISSERTACAO) || isEnrolledInPreviousExecutionPeriodOrAproved(INVESTIGACAO)) {
		removeCurricularCourse(curricularCoursesToBeEnrolledIn, TFCII);
		if(isEnrolledInPreviousExecutionPeriod(DISSERTACAO)) {
		    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO);
		}
		
	    } else if(isEnrolledInPreviousExecutionPeriod(TFCI)){
		removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO);
	    }
	    
	} else {
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(DEGREE));
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(MASTER_DEGREE));
	}
	
	return curricularCoursesToBeEnrolledIn;
    }

}
