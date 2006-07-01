package net.sourceforge.fenixedu.domain.accounting;

import java.math.BigDecimal;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class Entry extends Entry_Base {

    public static final Comparator<Entry> COMPARATOR_BY_MOST_RECENT_WHEN_BOOKED = new Comparator<Entry>() {
        public int compare(Entry entry, Entry otherEntry) {
            return -(entry.getWhenBooked().compareTo(otherEntry.getWhenBooked()));
        }

    };

    private Entry() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    Entry(EntryType entryType, BigDecimal amount, Account account) {
        this();
        init(new DateTime(), entryType, amount, account);
    }

    private void init(DateTime whenBooked, EntryType entryType, BigDecimal amount, Account account) {
        checkParameters(whenBooked, entryType, amount, account);
        super.setWhenBooked(whenBooked);
        super.setEntryType(entryType);
        super.setAmount(amount);
        super.setAccount(account);
    }

    private void checkParameters(DateTime whenBooked, EntryType entryType, BigDecimal amount,
            Account account) {
        if (whenBooked == null) {
            throw new DomainException("error.accounting.entry.invalid.whenBooked");
        }
        if (entryType == null) {
            throw new DomainException("error.accounting.entry.invalid.entryType");
        }
        if (amount == null) {
            throw new DomainException("error.accounting.entry.invalid.amount");
        }
        if (account == null) {
            throw new DomainException("error.accounting.entry.invalid.account");
        }
    }

    public boolean isPositiveAmount() {
        return getAmount().signum() > 0;
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
    public void setWhenBooked(DateTime whenBooked) {
        throw new DomainException("error.accounting.entry.cannot.modify.bookedDateTime");
    }

    @Override
    public void setEntryType(EntryType entryType) {
        throw new DomainException("error.accounting.entry.cannot.modify.entryType");
    }

    @Override
    public void setReceipt(Receipt receipt) {
        if (hasReceipt()) {
            throw new DomainException("error.accounting.entry.receipt.already.defined");
        } else {
            super.setReceipt(receipt);
        }
    }

    public String getDescription() {
        return getAccountingTransaction().getEvent().getDescriptionEntryType(getEntryType());
    }

}
