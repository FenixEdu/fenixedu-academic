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

public abstract class ServiceAgreementPaymentPlan extends ServiceAgreementPaymentPlan_Base {

    protected ServiceAgreementPaymentPlan() {
        super();
    }

    protected void init(final ExecutionYear executionYear, final ServiceAgreement serviceAgreement, final Boolean defaultPlan) {
        super.init(executionYear, defaultPlan);
        checkParameters(serviceAgreement);
        super.setServiceAgreement(serviceAgreement);
    }

    private void checkParameters(final ServiceAgreement serviceAgreement) {
        if (serviceAgreement == null) {
            throw new DomainException("error.accounting.ServiceAgreementPaymentPlan.serviceAgreement.cannot.be.null");
        }
    }

    @Override
    protected void removeParameters() {
        super.removeParameters();
        super.setServiceAgreement(null);
    }

    @Override
    public void setServiceAgreement(ServiceAgreement serviceAgreement) {
        throw new DomainException("error.accounting.ServiceAgreementPaymentPlan.cannot.modify.serviceAgreement");
    }

    @Override
    public ServiceAgreementTemplate getServiceAgreementTemplate() {
        return getServiceAgreement().getServiceAgreementTemplate();
    }

    @Deprecated
    public boolean hasServiceAgreement() {
        return getServiceAgreement() != null;
    }

}
