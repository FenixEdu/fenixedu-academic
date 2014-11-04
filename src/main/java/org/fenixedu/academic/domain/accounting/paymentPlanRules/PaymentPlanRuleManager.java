/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.accounting.paymentPlanRules;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PaymentPlanRuleManager {

    static final private Map<Class<? extends PaymentPlanRule>, PaymentPlanRule> rules =
            new HashMap<Class<? extends PaymentPlanRule>, PaymentPlanRule>();

    static {
        rules.put(HasEnrolmentsForExecutionSemesterPaymentPlanRule.class, new HasEnrolmentsForExecutionSemesterPaymentPlanRule());
        rules.put(HasEnrolmentsOnlyInSecondSemesterPaymentPlanRule.class, new HasEnrolmentsOnlyInSecondSemesterPaymentPlanRule());
        rules.put(IsPartialRegimePaymentPlanRule.class, new IsPartialRegimePaymentPlanRule());
        rules.put(FirstTimeInstitutionStudentsPaymentPlanRule.class, new FirstTimeInstitutionStudentsPaymentPlanRule());
        rules.put(IsAlienRule.class, new IsAlienRule());
    }

    static public void register(final Class<? extends PaymentPlanRule> clazz) {
        if (!containsRuleFor(clazz)) {
            register(PaymentPlanRuleFactory.create(clazz));
        }
    }

    static public void register(final PaymentPlanRule paymentPlanRule) {
        if (!containsRuleFor(paymentPlanRule)) {
            putRule(paymentPlanRule);
        }
    }

    static boolean containsRuleFor(final PaymentPlanRule paymentPlanRule) {
        return containsRuleFor(paymentPlanRule.getClass());
    }

    static public boolean containsRuleFor(Class<? extends PaymentPlanRule> clazz) {
        return rules.containsKey(clazz);
    }

    static public PaymentPlanRule getRule(Class<? extends PaymentPlanRule> clazz) {
        return rules.get(clazz);
    }

    static private void putRule(final PaymentPlanRule paymentPlanRule) {
        rules.put(paymentPlanRule.getClass(), paymentPlanRule);
    }

    static public Collection<PaymentPlanRule> getAllPaymentPlanRules() {
        return rules.values();
    }

}
