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

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.HasEnrolmentsOnlyInSecondSemesterPaymentPlanRule;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.PaymentPlanRule;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.PaymentPlanRuleFactory;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;

public class GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester extends
        GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester_Base {

    protected GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester() {
        super();
    }

    public GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester(final ExecutionYear executionYear,
            final DegreeCurricularPlanServiceAgreementTemplate serviceAgreementTemplate) {
        this(executionYear, serviceAgreementTemplate, false);
    }

    public GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester(final ExecutionYear executionYear,
            final DegreeCurricularPlanServiceAgreementTemplate serviceAgreementTemplate, final Boolean defaultPlan) {
        this();
        super.init(executionYear, serviceAgreementTemplate, defaultPlan);
    }

    @Override
    protected Collection<PaymentPlanRule> getSpecificPaymentPlanRules() {
        return Collections.singletonList(PaymentPlanRuleFactory.create(HasEnrolmentsOnlyInSecondSemesterPaymentPlanRule.class));
    }

}
