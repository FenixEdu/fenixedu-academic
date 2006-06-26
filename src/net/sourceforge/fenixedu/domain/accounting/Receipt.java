package net.sourceforge.fenixedu.domain.accounting;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class Receipt extends Receipt_Base {
    
    private Receipt() {
        super();
        super.setRootDomainObject(RootDomainObject.getInstance());
        super.setVersion(1);
        super.setWhenRegisted(new DateTime());
    }
    
    public Receipt(Party party, Entry ... entries) {
        this();
        init(party, entries);
    }

    private void init(Party party, Entry ... entries) {
        checkParameters(party);
        setParty(party);
        for (final Entry entry : entries) {
            entry.setReceipt(this);
        }
    }

    private void checkParameters(Party party) {
        if (party == null) {
            throw new DomainException("error.accounting.receipt.invalid.party");
        }
    }

    @Override
    public void addEntries(Entry entries) {
        throw new DomainException("error.accounting.receipt.cannot.add.new.entries");
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
        throw new DomainException("error.accounting.receipt.cannot.remove.entries");
    }

    @Override
    public void setVersion(Integer version) {
        throw new DomainException("error.accounting.receipt.cannot.modify.version");
    }
    
    @Override
    public void setWhenRegisted(DateTime whenRegisted) {
        throw new DomainException("error.accounting.receipt.cannot.modify.registedDateTime");
    }    
}
