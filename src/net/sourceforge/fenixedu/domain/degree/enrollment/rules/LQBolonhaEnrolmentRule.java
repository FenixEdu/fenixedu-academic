package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

public class LQBolonhaEnrolmentRule extends BolonhaEnrolmentRule {

    private static final String ESTAGIOA_CODE = "B5D";
    
    private static final String ESTAGIOB_CODE = "B5E";

    private static final String DISSERTACAO_CODE = "B8U";

    public LQBolonhaEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionSemester executionSemester) {
	super(studentCurricularPlan, executionSemester);    
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {

	if(isEnrolledInPreviousExecutionPeriodOrAproved(ESTAGIOA_CODE)) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, ESTAGIOB_CODE);
	} else {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	}

	return curricularCoursesToBeEnrolledIn;
    }
}
