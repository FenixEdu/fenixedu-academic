package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

public class LERCIBolonhaEnrolmentRule extends BolonhaEnrolmentRule {

    private static final String TFCI_CODE = "BB7";
    
    private static final String TFCII_CODE = "BBB";

    private static final String DISSERTACAO_CODE = "BB8";
    

    public LERCIBolonhaEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionSemester executionSemester) {
	super(studentCurricularPlan, executionSemester);    
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {
	
	if(isEnrolledInPreviousExecutionPeriodOrAproved(TFCI_CODE) || isEnrolledInExecutionPeriod(TFCII_CODE)) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	}

	if(isEnrolledInPreviousExecutionPeriodOrAproved(DISSERTACAO_CODE) || isEnrolledInExecutionPeriod(DISSERTACAO_CODE)) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, TFCII_CODE);
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	}

	return curricularCoursesToBeEnrolledIn;
    }
}
