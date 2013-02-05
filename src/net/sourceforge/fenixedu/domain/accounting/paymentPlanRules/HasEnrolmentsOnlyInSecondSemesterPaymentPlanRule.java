package net.sourceforge.fenixedu.domain.accounting.paymentPlanRules;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class HasEnrolmentsOnlyInSecondSemesterPaymentPlanRule implements PaymentPlanRule {

    HasEnrolmentsOnlyInSecondSemesterPaymentPlanRule() {
    }

    @Override
    public boolean isEvaluatedInNotSpecificPaymentRules() {
        return true;
    }

    @Override
    public boolean isAppliableFor(StudentCurricularPlan studentCurricularPlan, ExecutionYear executionYear) {
        return !studentCurricularPlan.hasAnyEnrolmentForExecutionPeriod(executionYear.getFirstExecutionPeriod())
                && studentCurricularPlan.hasAnyEnrolmentForExecutionPeriod(executionYear.getFirstExecutionPeriod()
                        .getNextExecutionPeriod());
    }

}
