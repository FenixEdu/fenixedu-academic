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
package org.fenixedu.academic.domain.accounting;

import java.util.Collection;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.paymentPlans.CustomGratuityPaymentPlan;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public abstract class ServiceAgreement extends ServiceAgreement_Base {

    static {
        getRelationServiceAgreementPerson().addListener(new RelationAdapter<ServiceAgreement, Person>() {
            @Override
            public void beforeAdd(ServiceAgreement serviceAgreementToAdd, Person person) {
                if (serviceAgreementToAdd != null && person != null) {
                    for (final ServiceAgreement serviceAgreement : person.getServiceAgreementsSet()) {
                        if (serviceAgreement.getServiceAgreementTemplate() == serviceAgreementToAdd.getServiceAgreementTemplate()) {
                            throw new DomainException(
                                    "error.accounting.ServiceAgreement.person.already.has.service.agreement.for.service.agreement.template");
                        }
                    }
                }
            }
        });
    }

    protected ServiceAgreement() {
        super();
        super.setCreationDate(new DateTime());
        super.setRootDomainObject(Bennu.getInstance());
    }

    protected void init(Person person, ServiceAgreementTemplate serviceAgreementTemplate) {
        checkParameters(person, serviceAgreementTemplate);
        super.setPerson(person);
        super.setServiceAgreementTemplate(serviceAgreementTemplate);
    }

    private void checkParameters(Person person, ServiceAgreementTemplate serviceAgreementTemplate) {
        if (person == null) {
            throw new DomainException("error.accounting.agreement.serviceAgreement.person.cannot.be.null");
        }
        if (serviceAgreementTemplate == null) {
            throw new DomainException("error.accounting.agreement.serviceAgreement.serviceAgreementTemplate.cannot.be.null");
        }
    }

    @Override
    public void setCreationDate(DateTime creationDate) {
        throw new DomainException("error.accounting.agreement.serviceAgreement.cannot.modify.creationDate");
    }

    @Override
    public void setServiceAgreementTemplate(ServiceAgreementTemplate serviceAgreementTemplate) {
        throw new DomainException("error.accounting.agreement.serviceAgreement.cannot.modify.serviceAgreementTemplate");
    }

    public void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());

        super.setPerson(null);
        super.setServiceAgreementTemplate(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!getPaymentPlansSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.ServiceAgreement.cannot.delete"));
        }
    }

    public CustomGratuityPaymentPlan getCustomGratuityPaymentPlan(final ExecutionYear executionYear) {
        for (final ServiceAgreementPaymentPlan paymentPlan : getPaymentPlansSet()) {
            if (paymentPlan instanceof CustomGratuityPaymentPlan && paymentPlan.isFor(executionYear)) {
                return (CustomGratuityPaymentPlan) paymentPlan;
            }
        }
        return null;
    }

    public boolean hasCustomGratuityPaymentPlan(final ExecutionYear executionYear) {
        return getCustomGratuityPaymentPlan(executionYear) != null;
    }

}
