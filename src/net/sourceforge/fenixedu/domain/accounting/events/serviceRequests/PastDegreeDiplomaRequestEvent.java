package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.util.Money;

public class PastDegreeDiplomaRequestEvent extends PastDegreeDiplomaRequestEvent_Base implements IPastRequestEvent {

    protected PastDegreeDiplomaRequestEvent() {
	super();
    }

    public PastDegreeDiplomaRequestEvent(final AdministrativeOffice administrativeOffice, final Person person,
	    final DiplomaRequest diplomaRequest) {
	this();
	super.init(administrativeOffice, EventType.PAST_DEGREE_DIPLOMA_REQUEST, person, diplomaRequest);
    }

    @Override
    public void setPastAmount(Money pastDegreeDiplomaRequestAmount) {
	throw new DomainException("error.accounting.events.cannot.modify.pastAmount");
    }

}
