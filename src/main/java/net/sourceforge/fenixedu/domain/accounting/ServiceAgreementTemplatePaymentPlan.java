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
package net.sourceforge.fenixedu.domain.accounting;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public abstract class ServiceAgreementTemplatePaymentPlan extends ServiceAgreementTemplatePaymentPlan_Base {

    static {
        getRelationServiceAgreementTemplateServiceAgreementTemplatePaymentPlan().addListener(
                new RelationAdapter<ServiceAgreementTemplate, ServiceAgreementTemplatePaymentPlan>() {
                    @Override
                    public void beforeAdd(ServiceAgreementTemplate serviceAgreementTemplate,
                            ServiceAgreementTemplatePaymentPlan paymentPlanToAdd) {

                        if (paymentPlanToAdd != null && serviceAgreementTemplate != null) {
                            if (paymentPlanToAdd.isDefault()
                                    && serviceAgreementTemplate.hasDefaultPaymentPlan(paymentPlanToAdd.getExecutionYear())) {
                                throw new DomainException(
                                        "error.domain.accounting.ServiceAgreementTemplate.already.has.a.default.payment.plan.for.execution.year");
                            }

                        }
                    }
                });
    }

    protected ServiceAgreementTemplatePaymentPlan() {
        super();
    }

    protected void init(final ExecutionYear executionYear, final ServiceAgreementTemplate serviceAgreementTemplate,
            final Boolean defaultPlan) {
        super.init(executionYear, defaultPlan);
        checkParameters(serviceAgreementTemplate);
        super.setServiceAgreementTemplate(serviceAgreementTemplate);
    }

    private void checkParameters(final ServiceAgreementTemplate serviceAgreementTemplate) {
        if (serviceAgreementTemplate == null) {
            throw new DomainException(
                    "error.accounting.ServiceAgreementTemplatePaymentPlan.serviceAgreementTemplate.cannot.be.null");
        }
    }

    @Override
    public void setServiceAgreementTemplate(ServiceAgreementTemplate serviceAgreementTemplate) {
        throw new DomainException("error.accounting.ServiceAgreementTemplatePaymentPlan.cannot.modify.serviceAgreementTemplate");
    }

    @Override
    protected void removeParameters() {
        super.removeParameters();
        super.setServiceAgreementTemplate(null);
    }

    @Deprecated
    public boolean hasServiceAgreementTemplate() {
        return getServiceAgreementTemplate() != null;
    }

}
