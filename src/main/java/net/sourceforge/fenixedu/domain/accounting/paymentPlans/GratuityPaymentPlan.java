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
package net.sourceforge.fenixedu.domain.accounting.paymentPlans;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.PaymentPlanRule;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;

public class GratuityPaymentPlan extends GratuityPaymentPlan_Base {

    protected GratuityPaymentPlan() {
        super();
    }

    public GratuityPaymentPlan(final ExecutionYear executionYear,
            final DegreeCurricularPlanServiceAgreementTemplate serviceAgreementTemplate) {
        this(executionYear, serviceAgreementTemplate, false);
    }

    public GratuityPaymentPlan(final ExecutionYear executionYear,
            final DegreeCurricularPlanServiceAgreementTemplate serviceAgreementTemplate, final Boolean defaultPlan) {
        this();
        super.init(executionYear, serviceAgreementTemplate, defaultPlan);
    }

    @Override
    public boolean isToApplyPenalty(Event event, Installment installment) {
        return !((GratuityEventWithPaymentPlan) event).hasPenaltyExemptionFor(installment);
    }

    @Override
    public boolean isGratuityPaymentPlan() {
        return true;
    }

    @Override
    protected Collection<PaymentPlanRule> getSpecificPaymentPlanRules() {
        return Collections.emptyList();
    }

    protected Set<Class<? extends GratuityPaymentPlan>> getPaymentPlansWhichHasPrecedence() {
        return Collections.EMPTY_SET;
    }

    public boolean hasPrecedenceOver(Class<? extends GratuityPaymentPlan> plan) {
        return getPaymentPlansWhichHasPrecedence().contains(plan);
    }

}
