package net.sourceforge.fenixedu.domain.accounting;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

import org.joda.time.DateTime;

public abstract class Event extends Event_Base {
    
    protected Event() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(getClass().getName());
    }
    
    protected void init(EventType eventType, DateTime whenOccured, Party party) {
        checkParameters(eventType, whenOccured, party);
        super.setEventType(eventType);
        super.setWhenOccured(whenOccured);
        super.setParty(party);
    }

    private void checkParameters(EventType eventType, DateTime whenOccured, Party party) throws DomainException {
        if (eventType == null) {
            throw new DomainException("error.accounting.event.invalid.eventType");
        }
        if (whenOccured == null) {
            throw new DomainException("error.accounting.event.invalid.dateTime");
        }
        if (party == null) {
            throw new DomainException("error.accounting.event.invalid.party");
        }
    }
    
    // TODO: to remove after create agreement and posting rules
    protected Entry makeEntry(BigDecimal amount, Account account) {
        return new Entry(amount, account, this);
    }
    
    // TODO: to remove after create agreement and posting rules
    protected void makeAccountingTransaction(Entry debit, Entry credit) {
        new AccountingTransaction(debit, credit);
    }
    
    // TODO: to remove after create agreement and posting rules
    protected void makeAccountingTransaction(Account from, Account to, BigDecimal amount) {
        new AccountingTransaction(makeEntry(amount.negate(), from), makeEntry(amount, to));
    }
    
    public boolean wasProcessed() {
        return getWhenNoticed() != null;
    }
    
    public final void process() {
        if (! wasProcessed()) {
            super.setWhenNoticed(new DateTime());
            internalProcess();
        }
    }
    
    protected abstract void internalProcess();
    
    public abstract List<EntryDTO> calculateEntries();

    @Override
    public void addEntries(Entry entries) {
        throw new DomainException("error.accounting.event.cannot.add.entries");
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
        throw new DomainException("error.accounting.event.cannot.remove.entries");
    }

    @Override
    public void setEventType(EventType eventType) {
        throw new DomainException("error.accounting.event.cannot.modify.eventType");
    }

    @Override
    public void setWhenNoticed(DateTime whenNoticed) {
        throw new DomainException("error.accounting.event.cannot.modify.noticedDateTime");
    }

    @Override
    public void setWhenOccured(DateTime whenOccured) {
        throw new DomainException("error.accounting.event.cannot.modify.occuredDateTime");
    }
    
    @Override
    public void setParty(Party party) {
        throw new DomainException("error.accounting.event.cannot.modify.party");
    }

}
