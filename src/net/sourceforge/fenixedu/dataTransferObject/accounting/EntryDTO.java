package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;
import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.Event;

public class EntryDTO implements Serializable {
    
    private DomainReference<Account> account;
    private DomainReference<Event> event;
    private BigDecimal amount;
    
    public EntryDTO(BigDecimal amount, Event event) {
        setAmount(amount);
        setEvent(event);
    }
    
    public EntryDTO(BigDecimal amount, Event event, Account account) {
        this(amount, event);
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
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
