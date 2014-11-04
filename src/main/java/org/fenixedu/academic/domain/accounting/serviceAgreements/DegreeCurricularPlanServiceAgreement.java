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
package org.fenixedu.academic.domain.accounting.serviceAgreements;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.PaymentPlan;
import org.fenixedu.academic.domain.accounting.paymentPlans.GratuityPaymentPlan;
import org.fenixedu.academic.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;

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
