package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;
import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;

public class EntryDTO implements Serializable {
    
    private DomainReference<Account> account;
    private DomainReference<Event> event;
    private EntryType entryType;
    private BigDecimal totalAmount;
    private BigDecimal payedAmount;
    private BigDecimal amountToPay;
    
    public EntryDTO(EntryType entryType, BigDecimal totalAmount, BigDecimal payedAmount, BigDecimal toPayAmount, Event event) {
        setTotalAmount(totalAmount);
        setPayedAmount(payedAmount);
        setAmountToPay(toPayAmount);
        setEvent(event);
        setEntryType(entryType);
    }
    
    public EntryDTO(EntryType entryType, BigDecimal totalAmount, BigDecimal payedAmount, BigDecimal toPayAmount, Event event, Account account) {
        this(entryType, totalAmount, payedAmount, toPayAmount, event);
        setAccount(account);
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

    public BigDecimal getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(BigDecimal toPayAmount) {
        this.amountToPay = toPayAmount;
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
}
