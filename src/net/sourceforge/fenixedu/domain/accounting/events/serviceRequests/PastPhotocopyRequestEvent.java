package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.PhotocopyRequest;
import net.sourceforge.fenixedu.util.Money;

public class PastPhotocopyRequestEvent extends PastPhotocopyRequestEvent_Base implements IPastRequestEvent {

    protected PastPhotocopyRequestEvent() {
	super();
    }

    public PastPhotocopyRequestEvent(final AdministrativeOffice administrativeOffice, final Person person,
	    final PhotocopyRequest request) {
	this();
	super.init(administrativeOffice, EventType.PAST_PHOTOCOPY_REQUEST, person, request);
    }

    @Override
    public void setPastAmount(Money amount) {
	throw new DomainException("error.accounting.events.cannot.modify.pastAmount");
    }

}
