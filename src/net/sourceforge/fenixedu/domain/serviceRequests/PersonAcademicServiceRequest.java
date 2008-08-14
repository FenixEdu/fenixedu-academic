package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

abstract public class PersonAcademicServiceRequest extends PersonAcademicServiceRequest_Base {

    protected PersonAcademicServiceRequest() {
	super();
    }

    protected void init(final Person person, final DateTime requestDate, final Boolean urgentRequest, final Boolean freeProcessed) {
	checkParameters(person);
	super.setPerson(person);
	super.init(requestDate, urgentRequest, freeProcessed);
    }

    private void checkParameters(final Person person) {
	if (person == null) {
	    throw new DomainException("error.serviceRequests.PersonAcademicServiceRequest.person.cannot.be.null");
	}
    }

    @Override
    public void setPerson(Person person) {
	throw new DomainException("error.serviceRequests.PersonAcademicServiceRequest.cannot.modify.person");
    }

    @Override
    public boolean isRequestForPerson() {
	return true;
    }

    @Override
    public void delete() {
	super.setPerson(null);
	super.delete();
    }
}
