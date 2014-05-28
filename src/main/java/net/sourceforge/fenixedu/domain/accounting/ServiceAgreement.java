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
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.paymentPlans.CustomGratuityPaymentPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public abstract class ServiceAgreement extends ServiceAgreement_Base {

    static {
        getRelationServiceAgreementPerson().addListener(new RelationAdapter<ServiceAgreement, Person>() {
            @Override
            public void beforeAdd(ServiceAgreement serviceAgreementToAdd, Person person) {
                if (serviceAgreementToAdd != null && person != null) {
                    for (final ServiceAgreement serviceAgreement : person.getServiceAgreements()) {
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
    public void setPerson(Person person) {
        throw new DomainException("error.accounting.agreement.serviceAgreement.cannot.modify.person");
    }

    @Override
    public void setServiceAgreementTemplate(ServiceAgreementTemplate serviceAgreementTemplate) {
        throw new DomainException("error.accounting.agreement.serviceAgreement.cannot.modify.serviceAgreementTemplate");
    }

    public void delete() {
        checkRulesToDelete();

        super.setPerson(null);
        super.setServiceAgreementTemplate(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    private void checkRulesToDelete() {
        if (hasAnyPaymentPlans()) {
            throw new DomainException("error.ServiceAgreement.cannot.delete");
        }
    }

    public CustomGratuityPaymentPlan getCustomGratuityPaymentPlan(final ExecutionYear executionYear) {
        for (final ServiceAgreementPaymentPlan paymentPlan : getPaymentPlans()) {
            if (paymentPlan instanceof CustomGratuityPaymentPlan && paymentPlan.isFor(executionYear)) {
                return (CustomGratuityPaymentPlan) paymentPlan;
            }
        }
        return null;
    }

    public boolean hasCustomGratuityPaymentPlan(final ExecutionYear executionYear) {
        return getCustomGratuityPaymentPlan(executionYear) != null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.ServiceAgreementPaymentPlan> getPaymentPlans() {
        return getPaymentPlansSet();
    }

    @Deprecated
    public boolean hasAnyPaymentPlans() {
        return !getPaymentPlansSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasServiceAgreementTemplate() {
        return getServiceAgreementTemplate() != null;
    }

    @Deprecated
    public boolean hasCreationDate() {
        return getCreationDate() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
