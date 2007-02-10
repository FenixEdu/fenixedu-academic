package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

public class LEAMBolonhaEnrolmentRule extends BolonhaEnrolmentRule {

    private static final String SISTEMAS_DISTRIBUICAO = "AES";
    
    private static final String TRATAMENTO_EFLUENTES = "AES";

    private static final String DISSERTACAO_CODE = "BAL";
    
    private static final String[] DEGREE = { "B61", "B32" };
    

    public LEAMBolonhaEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionPeriod executionPeriod) {
	super(studentCurricularPlan, executionPeriod);
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {
	
	if(isEnrolledInPreviousExecutionPeriodOrAproved(DISSERTACAO_CODE)) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(DEGREE));
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, TRATAMENTO_EFLUENTES);
	} else if(isEnrolledInExecutionPeriodOrAproved(DISSERTACAO_CODE)) {
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(DEGREE));
	}
	
	if(countEnroledOrAprovedEnrolments(DEGREE) > 0) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	}

	
	return curricularCoursesToBeEnrolledIn;
    }
}
