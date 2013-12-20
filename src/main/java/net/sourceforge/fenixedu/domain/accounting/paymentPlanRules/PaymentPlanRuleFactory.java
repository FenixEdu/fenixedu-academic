package net.sourceforge.fenixedu.domain.accounting.paymentPlanRules;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentPlanRuleFactory {

    private static final Logger logger = LoggerFactory.getLogger(PaymentPlanRuleFactory.class);

    static public PaymentPlanRule create(final Class<? extends PaymentPlanRule> clazz) {
        return PaymentPlanRuleManager.containsRuleFor(clazz) ? PaymentPlanRuleManager.getRule(clazz) : createRule(clazz);
    }

    private static PaymentPlanRule createRule(final Class<? extends PaymentPlanRule> clazz) {
        try {

            return clazz.newInstance();

        } catch (InstantiationException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }

        throw new DomainException("error.PaymentPlanRuleFactory.cannnot.create.rule");
    }

}
