package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.util.Money;

public class CreateOtherPartyPaymentBean implements Serializable {

    private DomainReference<Party> contributorParty;

    private DomainReference<Event> event;

    private Money amount;

    public CreateOtherPartyPaymentBean(final Event event) {
	setEvent(event);
    }

    public Party getContributorParty() {
	return (this.contributorParty != null) ? this.contributorParty.getObject() : null;
    }

    public void setContributorParty(Party contributor) {
	this.contributorParty = (contributor != null) ? new DomainReference<Party>(contributor) : null;
    }

    public Event getEvent() {
	return (this.event != null) ? this.event.getObject() : null;
    }

    public void setEvent(Event event) {
	this.event = (event != null) ? new DomainReference<Event>(event) : null;
    }

    public Money getAmount() {
	return amount;
    }

    public void setAmount(Money amount) {
	this.amount = amount;
    }

}
