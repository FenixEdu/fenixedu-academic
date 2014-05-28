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
