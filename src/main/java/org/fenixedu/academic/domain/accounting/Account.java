/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.accounting;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
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
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        super.setParty(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!getEntriesSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.accounting.account.cannot.be.deleted"));
        }
    }

    public void transferEntry(Entry entry) {
        super.addEntries(entry);
    }

    public boolean isInternal() {
        return getAccountType() == AccountType.INTERNAL;
    }

    public boolean isExternal() {
        return getAccountType() == AccountType.EXTERNAL;
    }

}
