/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
