package net.sourceforge.fenixedu.domain.accounting.paymentPlanRules;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class StudentIsInSecondCurricularYearPaymentPlanRule implements PaymentPlanRule {

    StudentIsInSecondCurricularYearPaymentPlanRule() {

    }

    @Override
    public boolean isEvaluatedInNotSpecificPaymentRules() {
	return false;
    }

    @Override
    public boolean isAppliableFor(StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {
	return (studentCurricularPlan.getRoot().getCurriculum(executionYear).getCurricularYear() == 2);
    }

}
