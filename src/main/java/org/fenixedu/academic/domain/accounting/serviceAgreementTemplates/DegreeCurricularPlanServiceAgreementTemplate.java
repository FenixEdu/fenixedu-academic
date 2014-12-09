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
import java.util.List;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.PaymentPlan;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplatePaymentPlan;
import org.fenixedu.academic.domain.accounting.paymentPlans.FullGratuityPaymentPlan;
import org.fenixedu.academic.domain.accounting.paymentPlans.GratuityPaymentPlan;
import org.fenixedu.academic.domain.accounting.paymentPlans.GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester;
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
        GratuityPaymentPlan result = null;
        for (final PaymentPlan paymentPlan : getPaymentPlansSet()) {
            if (paymentPlan instanceof GratuityPaymentPlan
                    && ((GratuityPaymentPlan) paymentPlan).isAppliableFor(studentCurricularPlan, executionYear)) {
                GratuityPaymentPlan gratuityPaymentPlan = (GratuityPaymentPlan) paymentPlan;

                if (result == null) {
                    result = (GratuityPaymentPlan) paymentPlan;
                } else {
                    if (gratuityPaymentPlan.hasPrecedenceOver(result.getClass())) {
                        result = gratuityPaymentPlan;
                    } else if (!result.hasPrecedenceOver(gratuityPaymentPlan.getClass())) {
                        // Incompatible payment plans
                        throw new DomainException(
                                "error.org.fenixedu.academic.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate.more.than.one.gratuity.payment.plan.is.appliable");
                    }
                }
            }
        }

        return result == null ? getDefaultPaymentPlan(executionYear) : result;
    }

    public List<GratuityPaymentPlan> getGratuityPaymentPlans() {
        final List<GratuityPaymentPlan> result = new ArrayList<GratuityPaymentPlan>();
        for (final PaymentPlan paymentPlan : getPaymentPlansSet()) {
            if (paymentPlan instanceof GratuityPaymentPlan) {
                result.add((GratuityPaymentPlan) paymentPlan);
            }
        }

        return result;
    }

    @Override
    public GratuityPaymentPlan getDefaultPaymentPlan(ExecutionYear executionYear) {
        return (GratuityPaymentPlan) super.getDefaultPaymentPlan(executionYear);
    }

    public boolean hasFullGratuityPaymentPlanFor(final ExecutionYear executionYear) {
        for (final PaymentPlan paymentPlan : getPaymentPlansSet()) {
            if (paymentPlan instanceof FullGratuityPaymentPlan && paymentPlan.getExecutionYear() == executionYear) {
                return true;
            }
        }
        return false;
    }

    public boolean hasGratuityPaymentPlanForStudentsEnroledOnlyInSecondSemesterBy(final ExecutionYear executionYear) {
        for (final PaymentPlan paymentPlan : getPaymentPlansSet()) {
            if (paymentPlan instanceof GratuityPaymentPlanForStudentsEnroledOnlyInSecondSemester
                    && paymentPlan.getExecutionYear() == executionYear) {
                return true;
            }
        }
        return false;
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
