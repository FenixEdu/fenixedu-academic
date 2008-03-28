package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.EquivalencePlanRequest;
import net.sourceforge.fenixedu.util.Money;

public class PastEquivalencePlanRequestEvent extends PastEquivalencePlanRequestEvent_Base implements IPastRequestEvent {

    protected PastEquivalencePlanRequestEvent() {
	super();
    }

    public PastEquivalencePlanRequestEvent(final AdministrativeOffice administrativeOffice, final Person person,
	    final EquivalencePlanRequest request) {
	this();
	super.init(administrativeOffice, EventType.PAST_EQUIVALENCE_PLAN_REQUEST, person, request);
    }

    @Override
    public void setPastAmount(Money pastAmount) {
	throw new DomainException("error.accounting.events.cannot.modify.pastAmount");
    }

}
