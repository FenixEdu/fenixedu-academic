package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;

public abstract class AcademicServiceRequestEvent extends AcademicServiceRequestEvent_Base {

    protected AcademicServiceRequestEvent() {
	super();
    }

    protected void init(AdministrativeOffice administrativeOffice, EventType eventType, Person person,
	    AcademicServiceRequest academicServiceRequest) {
	super.init(administrativeOffice, eventType, person);
	checkParameters(academicServiceRequest);
	super.setAcademicServiceRequest(academicServiceRequest);

    }

    private void checkParameters(AcademicServiceRequest academicServiceRequest) {
	if (academicServiceRequest == null) {
	    throw new DomainException("AcademicServiceRequestEvent.academicServiceRequest.cannot.be.null");
	}
    }

}
