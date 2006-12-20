package net.sourceforge.fenixedu.domain.accounting;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public abstract class ServiceAgreement extends ServiceAgreement_Base {

    static {
	ServiceAgreementPerson.addListener(new RelationAdapter<ServiceAgreement, Person>() {
	    @Override
	    public void beforeAdd(ServiceAgreement serviceAgreementToAdd, Person person) {
		if (serviceAgreementToAdd != null && person != null) {
		    for (final ServiceAgreement serviceAgreement : person.getServiceAgreements()) {
			if (serviceAgreement.getServiceAgreementTemplate() == serviceAgreementToAdd
				.getServiceAgreementTemplate()) {
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
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setOjbConcreteClass(getClass().getName());
    }

    protected void init(Person person, ServiceAgreementTemplate serviceAgreementTemplate) {
	checkParameters(person, serviceAgreementTemplate);
	super.setPerson(person);
	super.setServiceAgreementTemplate(serviceAgreementTemplate);
    }

    private void checkParameters(Person person, ServiceAgreementTemplate serviceAgreementTemplate) {
	if (person == null) {
	    throw new DomainException(
		    "error.accounting.agreement.serviceAgreement.person.cannot.be.null");
	}
	if (serviceAgreementTemplate == null) {
	    throw new DomainException(
		    "error.accounting.agreement.serviceAgreement.serviceAgreementTemplate.cannot.be.null");
	}
    }

    @Override
    public void setCreationDate(DateTime creationDate) {
	throw new DomainException(
		"error.accounting.agreement.serviceAgreement.cannot.modify.creationDate");
    }

    @Override
    public void setPerson(Person person) {
	throw new DomainException("error.accounting.agreement.serviceAgreement.cannot.modify.person");
    }

    @Override
    public void setServiceAgreementTemplate(ServiceAgreementTemplate serviceAgreementTemplate) {
	throw new DomainException(
		"error.accounting.agreement.serviceAgreement.cannot.modify.serviceAgreementTemplate");
    }

    public void delete() {
        super.setPerson(null);
        super.setServiceAgreementTemplate(null);
        removeRootDomainObject();
        deleteDomainObject();
    }

}
