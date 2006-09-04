package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

public class LEECBolonhaEnrolmentRule extends BolonhaEnrolmentRule {

    private static final String TFCI_CODE = "B65";

    private static final String DISSERTACAO_CODE = "$65";

    public LEECBolonhaEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionPeriod executionPeriod) {
	super(studentCurricularPlan, executionPeriod);
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {

	final boolean projecto = isEnrolledInExecutionPeriod(TFCI_CODE);
	final boolean dissertacao = isEnrolledInExecutionPeriod(DISSERTACAO_CODE);

	if (projecto) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	}
	if (dissertacao) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, TFCI_CODE);
	}

	return curricularCoursesToBeEnrolledIn;
    }

}
