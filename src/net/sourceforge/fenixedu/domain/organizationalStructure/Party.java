/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class Party extends Party_Base {

    public Party() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(getClass().getName());
        createAccount(AccountType.INTERNAL);
        createAccount(AccountType.EXTERNAL);
    }

    public Account createAccount(AccountType accountType) {
        checkAccountsFor(accountType);
        return new Account(accountType, this);
    }

    private void checkAccountsFor(AccountType accountType) {
        if (getAccountBy(accountType) != null) {
            throw new DomainException("error.party.accounts.accountType.already.exist");
        }
    }

    public Account getAccountBy(AccountType accountType) {
        for (final Account account : getAccountsSet()) {
            if (account.getAccountType() == accountType) {
                return account;
            }
        }
        return null;
    }

    public Set<Event> getNotPayedEvents() {

        final Set<Event> result = new HashSet<Event>();
        for (final Event event : getEventsSet()) {
            if (!event.isClosed()) {
                result.add(event);
            }
        }
        return result;
    }

    public Set<Entry> getPaymentsWithoutReceipt() {

        final Set<Entry> result = new HashSet<Entry>();
        for (final Event event : getEventsSet()) {
            result.addAll(event.getEntriesWithoutReceipt());
        }
        return result;
    }
}
