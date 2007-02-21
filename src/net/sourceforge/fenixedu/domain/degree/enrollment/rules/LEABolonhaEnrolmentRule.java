package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

public class LEABolonhaEnrolmentRule extends BolonhaEnrolmentRule {

    private static final String PROJECTOI_CODE = "A72";
    
    private static final String PROJECTOII_CODE = "A73";

    private static final String DISSERTACAO_CODE = "B80";
    
    private static final String HELI_CODE = "B52";

    public LEABolonhaEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionPeriod executionPeriod) {
	super(studentCurricularPlan, executionPeriod);
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {

	if(isEnrolledInPreviousExecutionPeriodOrAproved(PROJECTOI_CODE) || isEnrolledInExecutionPeriodOrAproved(PROJECTOII_CODE) 
		|| isEnrolledInExecutionPeriodOrAproved(HELI_CODE)) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	}

	if(isEnrolledInPreviousExecutionPeriodOrAproved(DISSERTACAO_CODE) || isEnrolledInExecutionPeriod(DISSERTACAO_CODE)) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, PROJECTOII_CODE);
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, HELI_CODE);
	}

	return curricularCoursesToBeEnrolledIn;
    }

}
