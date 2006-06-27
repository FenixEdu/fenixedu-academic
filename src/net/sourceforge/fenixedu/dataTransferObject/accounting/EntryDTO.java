package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;
import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;

public abstract class EntryDTO implements Serializable {

    private boolean selected;

    private DomainReference<Account> account;

    private DomainReference<Event> event;

    private EntryType entryType;

    private BigDecimal totalAmount;

    private BigDecimal payedAmount;

    public EntryDTO(EntryType entryType, BigDecimal totalAmount, BigDecimal payedAmount, Event event) {
        setTotalAmount(totalAmount);
        setPayedAmount(payedAmount);
        setEvent(event);
        setEntryType(entryType);
    }

    public Account getAccount() {
        return (this.account != null) ? this.account.getObject() : null;
    }

    public void setAccount(Account account) {
        this.account = (account != null) ? new DomainReference<Account>(account) : null;
    }

    public Event getEvent() {
        return (this.event != null) ? this.event.getObject() : null;
    }

    public void setEvent(Event event) {
        this.event = (event != null) ? new DomainReference<Event>(event) : null;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPayedAmount() {
        return payedAmount;
    }

    public void setPayedAmount(BigDecimal payedAmount) {
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
}
