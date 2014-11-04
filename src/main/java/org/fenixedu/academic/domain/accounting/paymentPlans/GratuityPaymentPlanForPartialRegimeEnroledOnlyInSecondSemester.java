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
package org.fenixedu.academic.domain.accounting.paymentPlans;

import java.util.Arrays;
import java.util.Collection;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.paymentPlanRules.HasEnrolmentsOnlyInSecondSemesterPaymentPlanRule;
import org.fenixedu.academic.domain.accounting.paymentPlanRules.IsPartialRegimePaymentPlanRule;
import org.fenixedu.academic.domain.accounting.paymentPlanRules.PaymentPlanRule;
import org.fenixedu.academic.domain.accounting.paymentPlanRules.PaymentPlanRuleFactory;
import org.fenixedu.academic.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;

public class GratuityPaymentPlanForPartialRegimeEnroledOnlyInSecondSemester extends
        GratuityPaymentPlanForPartialRegimeEnroledOnlyInSecondSemester_Base {

    protected GratuityPaymentPlanForPartialRegimeEnroledOnlyInSecondSemester() {
        super();
    }

    public GratuityPaymentPlanForPartialRegimeEnroledOnlyInSecondSemester(final ExecutionYear executionYear,
            final DegreeCurricularPlanServiceAgreementTemplate serviceAgreementTemplate, final Boolean defaultPaymentPlan) {
        this();
        init(executionYear, serviceAgreementTemplate, defaultPaymentPlan);
    }

    @Override
    protected Collection<PaymentPlanRule> getSpecificPaymentPlanRules() {
        return Arrays.asList(

        PaymentPlanRuleFactory.create(IsPartialRegimePaymentPlanRule.class),

        PaymentPlanRuleFactory.create(HasEnrolmentsOnlyInSecondSemesterPaymentPlanRule.class)

        );
    }

    @Override
    public boolean isForPartialRegime() {
        return true;
    }
}
