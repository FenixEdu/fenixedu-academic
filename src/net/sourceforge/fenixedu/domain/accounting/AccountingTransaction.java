package net.sourceforge.fenixedu.domain.accounting;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

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

    AccountingTransaction(User responsibleUser, Event event, Entry debit, Entry credit,
            PaymentMode paymentMode, DateTime whenRegistered) {
        this();
        init(responsibleUser, event, debit, credit, paymentMode, whenRegistered, null);
    }

    private AccountingTransaction(User responsibleUser, Entry debit, Entry credit,
            PaymentMode paymentMode, DateTime whenRegistered, AccountingTransaction transactionToAdjust) {
        this();
        init(responsibleUser, transactionToAdjust.getEvent(), debit, credit, paymentMode,
                whenRegistered, transactionToAdjust);
    }

    private void init(User responsibleUser, Event event, Entry debit, Entry credit,
            PaymentMode paymentMode, DateTime whenRegistered, AccountingTransaction transactionToAdjust) {
        checkParameters(responsibleUser, event, debit, credit, paymentMode);
        super.setWhenRegistered(whenRegistered);
        super.setWhenProcessed(new DateTime());
        super.setEvent(event);
        super.setResponsibleUser(responsibleUser);
        super.addEntries(debit);
        super.addEntries(credit);
        super.setPaymentMode(paymentMode);
        super.setAdjustedTransaction(transactionToAdjust);
    }

    private void checkParameters(User responsibleUser, Event event, Entry debit, Entry credit,
            PaymentMode paymentMode) {
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
        if (paymentMode == null) {
            throw new DomainException(
                    "error.accounting.accountingTransaction.paymentMode.cannot.be.null");
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
    public void setWhenRegistered(DateTime whenRegistered) {
        throw new DomainException("error.accounting.accountingTransaction.cannot.modify.whenRegistered");
    }

    @Override
    public void setWhenProcessed(DateTime whenProcessed) {
        throw new DomainException("error.accounting.accountingTransaction.cannot.modify.whenProcessed");
    }

    @Override
    public void setEvent(Event event) {
        throw new DomainException("error.accounting.accountingTransaction.cannot.modify.event");
    }

    @Override
    public void setResponsibleUser(User responsibleUser) {
        throw new DomainException("error.accounting.accountingTransaction.cannot.modify.responsibleUser");
    }

    @Override
    public void setAdjustedTransaction(AccountingTransaction adjustedTransaction) {
        throw new DomainException(
                "error.accounting.accountingTransaction.cannot.modify.adjustedTransaction");
    }

    @Override
    public void setAdjustmentTransaction(AccountingTransaction adjustementTransaction) {
        throw new DomainException(
                "error.accounting.accountingTransaction.cannot.modify.adjustmentTransaction");
    }

    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        return getEvent().getDescriptionForEntryType(entryType);
    }

    public Account getFromAccount() {
        return getEntry(false).getAccount();

    }

    public Account getToAccount() {
        return getEntry(true).getAccount();

    }

    public Entry getToAccountEntry() {
        return getEntry(true);
    }

    public Entry getFromAccountEntry() {
        return getEntry(false);
    }

    private Entry getEntry(boolean positive) {
        for (final Entry entry : getEntriesSet()) {
            if (entry.isPositiveAmount() == positive) {
                return entry;
            }
        }

        throw new DomainException("error.accounting.accountingTransaction.transaction.data.is.corrupted");
    }

    public AccountingTransaction reimburse(User responsibleUser, PaymentMode paymentMode,
            BigDecimal amountToReimburse) {
        final AccountingTransaction transaction = new AccountingTransaction(responsibleUser, new Entry(
                EntryType.ADJUSTMENT, amountToReimburse.negate(), getToAccount()), new Entry(
                EntryType.ADJUSTMENT, amountToReimburse, getFromAccount()), paymentMode, new DateTime(),
                this);

        getEvent().recalculateState(getWhenRegistered());

        return transaction;
    }

}
