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
import net.sourceforge.fenixedu.domain.accounting.PaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreement;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.paymentPlanRules.PaymentPlanRule;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreements.DegreeCurricularPlanServiceAgreement;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class CustomGratuityPaymentPlan extends CustomGratuityPaymentPlan_Base {

    static {
        getRelationServiceAgreementServiceAgreementPaymentPlan().addListener(
                new RelationAdapter<ServiceAgreement, ServiceAgreementPaymentPlan>() {
                    @Override
                    public void beforeAdd(ServiceAgreement serviceAgreement, ServiceAgreementPaymentPlan paymentPlanToAdd) {

                        if (paymentPlanToAdd != null) {
                            if (paymentPlanToAdd.isCustomGratuityPaymentPlan()
                                    && serviceAgreement.hasCustomGratuityPaymentPlan(paymentPlanToAdd.getExecutionYear())) {
                                throw new DomainException(
                                        "error.domain.accounting.ServiceAgreement.already.has.a.customGratuity.payment.plan.for.execution.year");
                            }

                        }
                    }
                });

        getRelationGratuityPaymentPlanGratuityEventWithPaymentPlan().addListener(
                new RelationAdapter<PaymentPlan, GratuityEventWithPaymentPlan>() {
                    @Override
                    public void beforeAdd(PaymentPlan paymentPlan, GratuityEventWithPaymentPlan event) {
                        if (paymentPlan != null && event != null) {
                            if (paymentPlan.isCustomGratuityPaymentPlan() && paymentPlan.hasAnyGratuityEventsWithPaymentPlan()) {
                                throw new DomainException("error.domain.accounting.PaymentPlan.already.has.gratuityEvent");
                            }
                        }
                    }
                });
    }

    private CustomGratuityPaymentPlan() {
        super();
    }

    public CustomGratuityPaymentPlan(final ExecutionYear executionYear,
            final DegreeCurricularPlanServiceAgreement serviceAgreement) {
        this();
        super.init(executionYear, serviceAgreement, Boolean.FALSE);
    }

    @Override
    public boolean isGratuityPaymentPlan() {
        return true;
    }

    @Override
    public boolean isCustomGratuityPaymentPlan() {
        return true;
    }

    @Override
    public void delete() {
        while (hasAnyInstallments()) {
            getInstallments().iterator().next().delete();
        }
        getGratuityEventsWithPaymentPlan().clear();
        removeParameters();
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Override
    protected Collection<PaymentPlanRule> getSpecificPaymentPlanRules() {
        return Collections.emptyList();
    }
}
