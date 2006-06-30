package net.sourceforge.fenixedu.domain.accounting;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

/**
 * Two-ledged accounting transaction
 * 
 * @author naat
 * 
 */
public class AccountingTransaction extends AccountingTransaction_Base {

    private AccountingTransaction() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    AccountingTransaction(User responsibleUser, Event event, Entry debit, Entry credit) {
        this();
        init(responsibleUser, event, debit, credit);
    }

    private void init(User responsibleUser, Event event, Entry debit, Entry credit) {
        checkParameters(responsibleUser, event, debit, credit);
        super.setWhenRegisted(credit.getWhenBooked());
        super.setEvent(event);
        super.setResponsibleUser(responsibleUser);
        super.addEntries(debit);
        super.addEntries(credit);
    }

    private void checkParameters(User responsibleUser, Event event, Entry debit, Entry credit) {
        if (event == null) {
            throw new DomainException("error.accounting.accountingTransaction.event.cannot.be.null");
        }
        if (responsibleUser == null) {
            throw new DomainException(
                    "error.accounting.accountingTransaction.responsibleUser.cannot.be.null");
        }
        if (debit == null) {
            throw new DomainException("error.accounting.accountingTransaction.debit.cannot.be.null");
        }
        if (credit == null) {
            throw new DomainException("error.accounting.accountingTransaction.credit.cannot.be.null");
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
        throw new DomainException(
                "error.accounting.accountingTransaction.cannot.modify.registedDateTime");
    }

    @Override
    public void setEvent(Event event) {
        throw new DomainException("error.accounting.accountingTransaction.cannot.modify.event");
    }

    @Override
    public void setResponsibleUser(User responsibleUser) {
        throw new DomainException("error.accounting.accountingTransaction.cannot.modify.responsibleUser");
    }

    public BigDecimal getAmountByAccount(Account account) {
        final Entry entryFromAccount = getEntryByAccount(account);

        if (entryFromAccount == null) {
            throw new DomainException(
                    "error.accounting.accountingTransaction.amount.not.available.because.account.does.not.participate.in.transaction");
        } else {
            return entryFromAccount.getAmount();
        }
    }

    public Entry getEntryByAccount(Account account) {
        for (final Entry entry : getEntriesSet()) {
            if (entry.getAccount() == account) {
                return entry;
            }
        }

        return null;
    }
}
