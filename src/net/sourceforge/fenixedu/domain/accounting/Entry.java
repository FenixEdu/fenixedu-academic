package net.sourceforge.fenixedu.domain.accounting;

import java.math.BigDecimal;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;

public class Entry extends Entry_Base {

    public static Comparator<Entry> COMPARATOR_BY_MOST_RECENT_WHEN_REGISTERED = new Comparator<Entry>() {
        public int compare(Entry leftEntry, Entry rightEntry) {
            int comparationResult = leftEntry.getWhenRegistered().compareTo(
                    rightEntry.getWhenRegistered());
            return (comparationResult == 0) ? leftEntry.getIdInternal().compareTo(
                    rightEntry.getIdInternal()) : comparationResult;
        }
    };

    private Entry() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    Entry(EntryType entryType, BigDecimal amount, Account account) {
        this();
        init(entryType, amount, account);
    }

    private void init(EntryType entryType, BigDecimal amount, Account account) {
        checkParameters(entryType, amount, account);
        super.setEntryType(entryType);
        super.setOriginalAmount(amount);
        super.setAccount(account);
    }

    private void checkParameters(EntryType entryType, BigDecimal amount, Account account) {
        if (entryType == null) {
            throw new DomainException("error.accounting.entry.entryType.cannot.be.null");
        }
        if (amount == null) {
            throw new DomainException("error.accounting.entry.originalAmount.cannot.be.null");
        }
        if (account == null) {
            throw new DomainException("error.accounting.entry.account.cannot.be.null");
        }
    }

    public boolean isPositiveAmount() {
        return getOriginalAmount().signum() > 0;
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
    public void setOriginalAmount(BigDecimal amount) {
        throw new DomainException("error.accounting.entry.cannot.modify.originalAmount");
    }

    @Override
    public void setEntryType(EntryType entryType) {
        throw new DomainException("error.accounting.entry.cannot.modify.entryType");
    }

    public DateTime getWhenRegistered() {
        return getAccountingTransaction().getWhenRegistered();
    }

    public DateTime getWhenProcessed() {
        return getAccountingTransaction().getWhenProcessed();
    }

    @Override
    public void setReceipt(Receipt receipt) {
        if (hasReceipt()) {
            throw new DomainException("error.accounting.entry.receipt.already.defined");
        } else {
            super.setReceipt(receipt);
        }
    }

    public LabelFormatter getDescription() {
        return getAccountingTransaction().getDescriptionForEntryType(getEntryType());
    }

    public boolean isAdjustement() {
        return (this.getAccountingTransaction().hasAdjustedTransaction());
    }

    public boolean isAdjusted() {
        return (this.getAccountingTransaction().hasAdjustmentTransaction());
    }

    public BigDecimal getAmountWithAdjustment() {
        return getOriginalAmount();
    }

}
