package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.util.Money;

public class PastDegreeDiplomaRequestEvent extends PastDegreeDiplomaRequestEvent_Base {

    protected PastDegreeDiplomaRequestEvent() {
	super();
    }

    public PastDegreeDiplomaRequestEvent(final AdministrativeOffice administrativeOffice, final EventType eventType,
	    final Person person, final DiplomaRequest diplomaRequest) {
	this();
	super.init(administrativeOffice, eventType, person, diplomaRequest);
    }

    @Override
    public void setPastAmount(Money pastDegreeDiplomaRequestAmount) {
	throw new DomainException("error.accounting.events.gratuity.PastDegreeDiplomaRequestEvent.cannot.modify.pastAmount");
    }

}
