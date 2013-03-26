package net.sourceforge.fenixedu.domain.accounting.paymentPlanRules;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

/**
 * Represents a payment plan rule that is used to select some payment plan to
 * student gratuity event
 * 
 * <pre>
 * - you must register each rule in @see #PaymentPlanRuleManager
 * </pre>
 */
abstract public interface PaymentPlanRule {

    /**
     * Indicates if rule can be evaluated when testing not specific payment
     * rules for payment plan. @see #PaymentPlanRule.isAppliableFor
     */
    abstract public boolean isEvaluatedInNotSpecificPaymentRules();

    abstract public boolean isAppliableFor(final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear);

}
