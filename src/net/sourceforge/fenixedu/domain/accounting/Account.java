package net.sourceforge.fenixedu.domain.accounting;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

import org.joda.time.DateTime;

public class Account extends Account_Base {

    private Account() {
	super();
	super.setCreationDate(new DateTime());
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Account(AccountType accountType, Party party) {
	this();
	init(accountType, party);
    }

    private void init(AccountType accountType, Party party) {
	checkParameters(accountType, party);
	super.setAccountType(accountType);
	super.setParty(party);
    }

    private void checkParameters(AccountType accountType, Party party) {
	if (accountType == null) {
	    throw new DomainException("error.accounting.account.invalid.accountType");
	}
	if (party == null) {
	    throw new DomainException("error.accounting.account.invalid.party");
	}
    }

    @Override
    public void addEntries(Entry entries) {
	throw new DomainException("error.accounting.account.cannot.add.entries");
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
	throw new DomainException("error.accounting.account.cannot.remove.entries");
    }

    @Override
    public void setAccountType(AccountType accountType) {
	throw new DomainException("error.accounting.account.cannot.modify.accountType");
    }

    @Override
    public void setParty(Party party) {
	throw new DomainException("error.accounting.account.cannot.modify.party");
    }

    @Override
    public void setCreationDate(DateTime creationDate) {
	throw new DomainException("error.accounting.account.cannot.modify.creationDate");
    }

    public void delete() {
	if (!canBeDeleted()) {
	    throw new DomainException("error.accounting.account.cannot.be.deleted");
	}
	super.setParty(null);
	removeRootDomainObject();
	deleteDomainObject();
    }

    private boolean canBeDeleted() {
	return getEntriesCount() == 0;
    }
}
