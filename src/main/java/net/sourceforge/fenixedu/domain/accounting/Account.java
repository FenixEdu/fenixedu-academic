package net.sourceforge.fenixedu.domain.accounting;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class Account extends Account_Base {

    private Account() {
        super();
        super.setCreationDate(new DateTime());
        setRootDomainObject(Bennu.getInstance());
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
        setRootDomainObject(null);
        deleteDomainObject();
    }

    private boolean canBeDeleted() {
        return getEntriesSet().size() == 0;
    }

    public void transferEntry(Entry entry) {
        if (!AccessControl.getPerson().hasRole(RoleType.MANAGER)) {
            throw new DomainException("permission.denied");
        }

        super.addEntries(entry);
    }

    public boolean isInternal() {
        return getAccountType() == AccountType.INTERNAL;
    }

    public boolean isExternal() {
        return getAccountType() == AccountType.EXTERNAL;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.Entry> getEntries() {
        return getEntriesSet();
    }

    @Deprecated
    public boolean hasAnyEntries() {
        return !getEntriesSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasParty() {
        return getParty() != null;
    }

    @Deprecated
    public boolean hasCreationDate() {
        return getCreationDate() != null;
    }

    @Deprecated
    public boolean hasAccountType() {
        return getAccountType() != null;
    }

}
