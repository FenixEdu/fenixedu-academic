package net.sourceforge.fenixedu.domain.accounting.paymentPlanRules;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PaymentPlanRuleFactory {

	static public PaymentPlanRule create(final Class<? extends PaymentPlanRule> clazz) {
		return PaymentPlanRuleManager.containsRuleFor(clazz) ? PaymentPlanRuleManager.getRule(clazz) : createRule(clazz);
	}

	private static PaymentPlanRule createRule(final Class<? extends PaymentPlanRule> clazz) {
		try {

			return clazz.newInstance();

		} catch (InstantiationException e) {
			e.printStackTrace();

		} catch (IllegalAccessException e) {
			e.printStackTrace();

		}

		throw new DomainException("error.PaymentPlanRuleFactory.cannnot.create.rule");
	}

}
