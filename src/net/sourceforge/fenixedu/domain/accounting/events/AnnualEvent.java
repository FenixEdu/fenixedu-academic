package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;

public abstract class AnnualEvent extends AnnualEvent_Base {

    protected AnnualEvent() {
	super();
    }

    protected void init(EventType eventType, Person person, ExecutionYear executionYear) {
	init(null, eventType, person, executionYear);
    }

    protected void init(AdministrativeOffice administrativeOffice, EventType eventType, Person person,
	    ExecutionYear executionYear) {
	super.init(administrativeOffice, eventType, person, (AcademicServiceRequest) null);
	checkParameters(executionYear);
	super.setExecutionYear(executionYear);

    }

    private void checkParameters(ExecutionYear executionYear) {
	if (executionYear == null) {
	    throw new DomainException("error.accounting.events.AnnualEvent.executionYear.cannot.be.null");
	}

    }

}
