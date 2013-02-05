package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.util.Money;

public class CreateOtherPartyPaymentBean implements Serializable {

    private Party contributorParty;

    private Event event;

    private Money amount;

    public CreateOtherPartyPaymentBean(final Event event) {
        setEvent(event);
    }

    public Party getContributorParty() {
        return this.contributorParty;
    }

    public void setContributorParty(Party contributor) {
        this.contributorParty = contributor;
    }

    public Event getEvent() {
        return this.event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

}
