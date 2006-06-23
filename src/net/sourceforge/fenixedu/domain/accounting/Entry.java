package net.sourceforge.fenixedu.domain.accounting;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class Entry extends Entry_Base {
    
    private Entry() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    Entry(BigDecimal amount, Account account, Event event) {
        this();
        init(event.getWhenNoticed(), amount, account, event);
    }

    private void init(DateTime whenBooked, BigDecimal amount, Account account, Event event) {
        checkParameters(whenBooked, amount, account, event);
        super.setWhenBooked(whenBooked);
        super.setAmount(amount);
        super.setAccount(account);
        super.setEvent(event);
    }

    private void checkParameters(DateTime whenBooked, BigDecimal amount, Account account, Event event) {
        if (whenBooked == null) {
            throw new DomainException("error.accounting.entry.invalid.whenBooked");
        }
        if (amount == null) {
            throw new DomainException("error.accounting.entry.invalid.amount");
        }
        if (account == null && event == null) {
            throw new DomainException("error.accounting.entry.invalid.account.and.entry");
        }
    }

    @Override
    public void removeAccount() {
        throw new DomainException("error.accounting.entry.cannot.remove.account");
    }

    @Override
    public void removeAccountingTransaction() {
        throw new DomainException("error.accounting.entry.cannot.remove.accountingTransaction");
    }

    @Override
    public void removeEvent() {
        throw new DomainException("error.accounting.entry.cannot.remove.event");
    }

    @Override
    public void setAccount(Account account) {
        throw new DomainException("error.accounting.entry.cannot.modify.account");
    }

    @Override
    public void setAccountingTransaction(AccountingTransaction accountingTransaction) {
        throw new DomainException("error.accounting.entry.cannot.modify.accountingTransaction");
    }

    @Override
    public void setAmount(BigDecimal amount) {
        throw new DomainException("error.accounting.entry.cannot.modify.amount");
    }

    @Override
    public void setEvent(Event event) {
        throw new DomainException("error.accounting.entry.cannot.modify.event");
    }

    @Override
    public void setWhenBooked(DateTime whenBooked) {
        throw new DomainException("error.accounting.entry.cannot.modify.bookedDateTime");
    }
    
}
