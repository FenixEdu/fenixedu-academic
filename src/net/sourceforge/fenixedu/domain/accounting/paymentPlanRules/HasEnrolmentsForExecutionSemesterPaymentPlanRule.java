package net.sourceforge.fenixedu.domain.accounting.paymentPlanRules;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class HasEnrolmentsForExecutionSemesterPaymentPlanRule implements PaymentPlanRule {

	HasEnrolmentsForExecutionSemesterPaymentPlanRule() {
	}

	@Override
	public boolean isEvaluatedInNotSpecificPaymentRules() {
		return false;
	}

	@Override
	public boolean isAppliableFor(StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {
		return studentCurricularPlan.hasAnyEnrolmentForExecutionPeriod(executionYear.getFirstExecutionPeriod());
	}

}
