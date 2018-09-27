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
package org.fenixedu.academic.domain.accounting.serviceAgreementTemplates;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.PaymentPlan;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplatePaymentPlan;
import org.fenixedu.academic.domain.accounting.paymentPlans.GratuityPaymentPlan;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;

public class DegreeCurricularPlanServiceAgreementTemplate extends DegreeCurricularPlanServiceAgreementTemplate_Base {

    private DegreeCurricularPlanServiceAgreementTemplate() {
        super();
    }

    public DegreeCurricularPlanServiceAgreementTemplate(DegreeCurricularPlan degreeCurricularPlan) {
        this();
        init(degreeCurricularPlan);
    }

    private void checkParameters(DegreeCurricularPlan degreeCurricularPlan) {
        if (degreeCurricularPlan == null) {
            throw new DomainException(
                    "error.accounting.agreement.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate.degreeCurricularPlan.cannot.be.null");
        }

    }

    protected void init(DegreeCurricularPlan degreeCurricularPlan) {
        checkParameters(degreeCurricularPlan);
        checkRulesToCreate(degreeCurricularPlan);
        super.setDegreeCurricularPlan(degreeCurricularPlan);
    }

    private void checkRulesToCreate(DegreeCurricularPlan degreeCurricularPlan) {
        if (readByDegreeCurricularPlan(degreeCurricularPlan) != null) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate.degree.curricular.plan.already.has.template.defined");
        }

    }

    @Override
    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        throw new DomainException(
                "error.accounting.agreement.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate.cannot.modify.degreeCurricularPlan");
    }

    public GratuityPaymentPlan getGratuityPaymentPlanFor(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionYear executionYear) {

        return getPaymentPlansSet().stream()
                .filter(GratuityPaymentPlan.class::isInstance)
                .map(GratuityPaymentPlan.class::cast)
                .filter(p -> p.isApplicableFor(studentCurricularPlan,executionYear))
                .min(Comparator.comparingInt(p1 -> PaymentPlan.getPrecedenceOrder().indexOf(p1.getClass())))
                .orElseGet(() -> getDefaultPaymentPlan(executionYear));
    }

    @Override
    public GratuityPaymentPlan getDefaultPaymentPlan(ExecutionYear executionYear) {
        return (GratuityPaymentPlan) super.getDefaultPaymentPlan(executionYear);
    }

    public static DegreeCurricularPlanServiceAgreementTemplate readByDegreeCurricularPlan(
            final DegreeCurricularPlan degreeCurricularPlan) {
        for (final ServiceAgreementTemplate serviceAgreementTemplate : Bennu.getInstance().getServiceAgreementTemplatesSet()) {

            if (serviceAgreementTemplate instanceof DegreeCurricularPlanServiceAgreementTemplate) {
                final DegreeCurricularPlanServiceAgreementTemplate degreeCurricularPlanServiceAgreementTemplate =
                        (DegreeCurricularPlanServiceAgreementTemplate) serviceAgreementTemplate;

                if (degreeCurricularPlanServiceAgreementTemplate.getDegreeCurricularPlan() == degreeCurricularPlan) {
                    return degreeCurricularPlanServiceAgreementTemplate;
                }
            }
        }

        return null;
    }

    public List<GratuityPaymentPlan> getGratuityPaymentPlansFor(final ExecutionYear executionYear) {
        final List<GratuityPaymentPlan> result = new ArrayList<GratuityPaymentPlan>();

        for (final ServiceAgreementTemplatePaymentPlan paymentPlan : getPaymentPlansSet()) {
            if (paymentPlan instanceof GratuityPaymentPlan && paymentPlan.isFor(executionYear)) {
                result.add((GratuityPaymentPlan) paymentPlan);
            }
        }

        return result;
    }

}
