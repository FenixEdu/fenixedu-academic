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
package net.sourceforge.fenixedu.domain.accounting.serviceAgreements;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.PaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.paymentPlans.GratuityPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;

public class DegreeCurricularPlanServiceAgreement extends DegreeCurricularPlanServiceAgreement_Base {

    private DegreeCurricularPlanServiceAgreement() {
        super();
    }

    public DegreeCurricularPlanServiceAgreement(Person person,
            DegreeCurricularPlanServiceAgreementTemplate degreeCurricularPlanServiceAgreementTemplate) {
        this();
        super.init(person, degreeCurricularPlanServiceAgreementTemplate);
    }

    @Override
    public DegreeCurricularPlanServiceAgreementTemplate getServiceAgreementTemplate() {
        return (DegreeCurricularPlanServiceAgreementTemplate) super.getServiceAgreementTemplate();
    }

    public GratuityPaymentPlan getGratuityPaymentPlanFor(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear) {
        return getServiceAgreementTemplate().getGratuityPaymentPlanFor(studentCurricularPlan, executionYear);
    }

    public List<GratuityPaymentPlan> getGratuityPaymentPlans() {
        final List<GratuityPaymentPlan> result = new ArrayList<GratuityPaymentPlan>();
        for (final PaymentPlan paymentPlan : getServiceAgreementTemplate().getPaymentPlansSet()) {
            if (paymentPlan instanceof GratuityPaymentPlan) {
                result.add((GratuityPaymentPlan) paymentPlan);
            }
        }

        return result;
    }

    public GratuityPaymentPlan getDefaultGratuityPaymentPlan(final ExecutionYear executionYear) {
        return getServiceAgreementTemplate().getDefaultPaymentPlan(executionYear);
    }
}
