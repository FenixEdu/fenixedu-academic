package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class EntryDTO implements Serializable {

    private boolean selected;

    private EntryType entryType;

    private DomainReference<Event> event;

    private Money totalAmount;

    private Money payedAmount;

    private Money amountToPay;

    private LabelFormatter description;

    private Money debtAmount;

    public EntryDTO(EntryType entryType, Event event, Money totalAmount, Money payedAmount,
	    Money amountToPay, LabelFormatter description, Money debtAmount) {
	setEntryType(entryType);
	setEvent(event);
	setTotalAmount(totalAmount);
	setPayedAmount(payedAmount);
	setAmountToPay(amountToPay);
	setDescription(description);
	setDebtAmount(debtAmount);

    }
    
    public EntryDTO(EntryType entryType, Event event, Money amountToPay) {
	setEntryType(entryType);
	setEvent(event);
	setAmountToPay(amountToPay);
    }

    public Money getTotalAmount() {
	return totalAmount;
    }

    public void setTotalAmount(Money totalAmount) {
	this.totalAmount = totalAmount;
    }

    public Money getPayedAmount() {
	return payedAmount;
    }

    public void setPayedAmount(Money payedAmount) {
	this.payedAmount = payedAmount;
    }

    public EntryType getEntryType() {
	return entryType;
    }

    public void setEntryType(EntryType entryType) {
	this.entryType = entryType;
    }

    public boolean isSelected() {
	return selected;
    }

    public void setSelected(boolean selected) {
	this.selected = selected;
    }

    public Money getAmountToPay() {
	return amountToPay;
    }

    public void setAmountToPay(Money amountToPay) {
	this.amountToPay = amountToPay;
    }

    public Event getEvent() {
	return (this.event != null) ? this.event.getObject() : null;
    }

    public void setEvent(Event event) {
	this.event = (event != null) ? new DomainReference<Event>(event) : null;
    }

    public LabelFormatter getDescription() {
	return description;
    }

    public void setDescription(LabelFormatter description) {
	this.description = description;
    }

    public void setDebtAmount(Money debtAmount) {
	this.debtAmount = debtAmount;
    }

    public Money getDebtAmount() {
	return this.debtAmount;
    }

}
