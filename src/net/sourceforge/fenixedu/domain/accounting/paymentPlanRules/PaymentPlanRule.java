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

    abstract public boolean isAppliableFor(final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear);

}
