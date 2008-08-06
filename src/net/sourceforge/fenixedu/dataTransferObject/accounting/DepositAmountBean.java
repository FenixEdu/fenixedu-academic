package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class DepositAmountBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private EntryType entryType;

    private DomainReference<Event> event;

    private Money amount;

    private DateTime whenRegistered;

    private String reason;

    public DepositAmountBean(final Event event) {
	setEvent(event);
	setWhenRegistered(new DateTime());
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

    public DateTime getWhenRegistered() {
	return whenRegistered;
    }

    public void setWhenRegistered(DateTime whenRegistered) {
	this.whenRegistered = whenRegistered;
    }

    public String getReason() {
	return reason;
    }

    public void setReason(String reason) {
	this.reason = reason;
    }

    public void setEvent(DomainReference<Event> event) {
	this.event = event;
    }

    public EntryType getEntryType() {
	return entryType;
    }

    public void setEntryType(EntryType entryType) {
	this.entryType = entryType;
    }

}
