package net.sourceforge.fenixedu.domain.accounting;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class AccountingTransaction extends AccountingTransaction_Base {
    
    private AccountingTransaction() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    AccountingTransaction(Entry debit, Entry credit) {
        this();
        init(debit, credit);     
    }

    private void init(Entry debit, Entry credit) {
        checkParameters(debit, credit);
        super.setWhenRegisted(credit.getWhenBooked());
        super.addEntries(debit);
        super.addEntries(credit);
    }
    
    private void checkParameters(Entry debit, Entry credit) {
        if (debit == null) {
            throw new DomainException("error.accounting.accountingTransaction.invalid.debitEntry");
        }
        if (credit == null) {
            throw new DomainException("error.accounting.accountingTransaction.invalid.creditEntry");
        }
    }

    @Override
    public void addEntries(Entry entries) {
        throw new DomainException("error.accounting.accountingTransaction.cannot.add.entries");
    }

    @Override
    public List<Entry> getEntries() {
        return Collections.unmodifiableList(super.getEntries());
    }

    @Override
    public Iterator<Entry> getEntriesIterator() {
        return getEntriesSet().iterator();
    }

    @Override
    public Set<Entry> getEntriesSet() {
        return Collections.unmodifiableSet(super.getEntriesSet());
    }

    @Override
    public void removeEntries(Entry entries) {
        throw new DomainException("error.accounting.accountingTransaction.cannot.remove.entries");
    }
    
    @Override
    public void setWhenRegisted(DateTime whenRegisted) {
        throw new DomainException("error.accounting.accountingTransaction.cannot.modify.registedDateTime");
    }
}
