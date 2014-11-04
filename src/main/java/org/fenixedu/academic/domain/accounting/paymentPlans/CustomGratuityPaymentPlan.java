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

import java.util.Collection;
import java.util.Collections;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accounting.PaymentPlan;
import org.fenixedu.academic.domain.accounting.ServiceAgreement;
import org.fenixedu.academic.domain.accounting.ServiceAgreementPaymentPlan;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import org.fenixedu.academic.domain.accounting.paymentPlanRules.PaymentPlanRule;
import org.fenixedu.academic.domain.accounting.serviceAgreements.DegreeCurricularPlanServiceAgreement;
import org.fenixedu.academic.domain.exceptions.DomainException;

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
                            if (paymentPlan.isCustomGratuityPaymentPlan()
                                    && !paymentPlan.getGratuityEventsWithPaymentPlanSet().isEmpty()) {
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
        while (!getInstallmentsSet().isEmpty()) {
            getInstallmentsSet().iterator().next().delete();
        }
        getGratuityEventsWithPaymentPlanSet().clear();
        removeParameters();
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Override
    protected Collection<PaymentPlanRule> getSpecificPaymentPlanRules() {
        return Collections.emptyList();
    }
}
