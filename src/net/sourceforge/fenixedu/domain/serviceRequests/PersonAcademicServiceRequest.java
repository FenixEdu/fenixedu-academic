package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

abstract public class PersonAcademicServiceRequest extends PersonAcademicServiceRequest_Base {

    protected PersonAcademicServiceRequest() {
	super();
    }

    protected void init(final Person person, final AcademicServiceRequestCreateBean bean) {
	checkParameters(person);
	super.setPerson(person);

	super.init(bean);
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
    protected void disconnect() {
	super.setPerson(null);
	super.disconnect();
    }

}
