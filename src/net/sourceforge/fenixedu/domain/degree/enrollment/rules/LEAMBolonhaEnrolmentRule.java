package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

public class LEAMBolonhaEnrolmentRule extends BolonhaEnrolmentRule {

    private static final String SISTEMAS_DISTRIBUICAO = "AES";

    private static final String DISSERTACAO_CODE = "$134";

    public LEAMBolonhaEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionPeriod executionPeriod) {
	super(studentCurricularPlan, executionPeriod);
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {

	final boolean sistemas = isEnrolledInExecutionPeriod(SISTEMAS_DISTRIBUICAO);
	final boolean dissertacao = isEnrolledInExecutionPeriod(DISSERTACAO_CODE);

	if (sistemas) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	}
	
	if (dissertacao) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, SISTEMAS_DISTRIBUICAO);
	}
	
	return curricularCoursesToBeEnrolledIn;
    }
}
