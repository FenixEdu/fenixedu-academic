package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

public class LEECBolonhaEnrolmentRule extends BolonhaEnrolmentRule {

    private static final String TFCI_CODE = "B65";
    
    private static final String TFCII_CODE = "B66";
    
    private static final String TFCIII_CODE = "B67";

    private static final String DISSERTACAO_CODE = "B86";

    public LEECBolonhaEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionPeriod executionPeriod) {
	super(studentCurricularPlan, executionPeriod);
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {

	if(isEnrolledInPreviousExecutionPeriodOrAproved(TFCI_CODE)) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	}
	
	if(isEnrolledInExecutionPeriod(DISSERTACAO_CODE) || isEnrolledInPreviousExecutionPeriodOrAproved(DISSERTACAO_CODE)) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, TFCII_CODE);
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, TFCIII_CODE);
	}
	
	return curricularCoursesToBeEnrolledIn;
    }

}
