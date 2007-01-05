package net.sourceforge.fenixedu.domain.accounting;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;

/**
 * Two-ledged accounting transaction
 * 
 * @author naat
 * 
 */
public class AccountingTransaction extends AccountingTransaction_Base {

    public static Comparator<AccountingTransaction> COMPARATOR_BY_WHEN_REGISTERED = new Comparator<AccountingTransaction>() {
	public int compare(AccountingTransaction leftAccountingTransaction,
		AccountingTransaction rightAccountingTransaction) {
	    int comparationResult = leftAccountingTransaction.getWhenRegistered().compareTo(
		    rightAccountingTransaction.getWhenRegistered());
	    return (comparationResult == 0) ? leftAccountingTransaction.getIdInternal().compareTo(
		    rightAccountingTransaction.getIdInternal()) : comparationResult;
	}
    };

    protected AccountingTransaction() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setOjbConcreteClass(getClass().getName());
    }

    public AccountingTransaction(User responsibleUser, Event event, Entry debit, Entry credit,
	    AccountingTransactionDetail transactionDetail) {
	this();
	init(responsibleUser, event, debit, credit, transactionDetail);
    }

    private AccountingTransaction(User responsibleUser, Entry debit, Entry credit,
	    AccountingTransactionDetail transactionDetail, AccountingTransaction transactionToAdjust) {
	this();
	init(responsibleUser, transactionToAdjust.getEvent(), debit, credit, transactionDetail,
		transactionToAdjust);
    }

    protected void init(User responsibleUser, Event event, Entry debit, Entry credit,
	    AccountingTransactionDetail transactionDetail) {
	init(responsibleUser, event, debit, credit, transactionDetail, null);
    }

    protected void init(User responsibleUser, Event event, Entry debit, Entry credit,
	    AccountingTransactionDetail transactionDetail, AccountingTransaction transactionToAdjust) {

	checkParameters(responsibleUser, event, debit, credit);

	super.setEvent(event);
	super.setResponsibleUser(responsibleUser);
	super.addEntries(debit);
	super.addEntries(credit);
	super.setAdjustedTransaction(transactionToAdjust);

	super.setTransactionDetail(transactionDetail);
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
    public void setTransactionDetail(AccountingTransactionDetail transactionDetail) {
	throw new DomainException(
		"error.accounting.AccountingTransaction.cannot.modify.transactionDetail");
    }

    @Override
    public void addAdjustmentTransactions(AccountingTransaction accountingTransaction) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.AccountingTransaction.cannot.add.accountingTransaction");
    }

    @Override
    public List<AccountingTransaction> getAdjustmentTransactions() {
	return Collections.unmodifiableList(super.getAdjustmentTransactions());
    }

    @Override
    public Set<AccountingTransaction> getAdjustmentTransactionsSet() {
	return Collections.unmodifiableSet(super.getAdjustmentTransactionsSet());
    }

    @Override
    public Iterator<AccountingTransaction> getAdjustmentTransactionsIterator() {
	return getAdjustmentTransactionsSet().iterator();
    }

    @Override
    public void removeAdjustmentTransactions(AccountingTransaction adjustmentTransactions) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.AccountingTransaction.cannot.remove.accountingTransaction");
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
	    Money amountToReimburse) {

	if (!canApplyReimbursement(amountToReimburse)) {
	    throw new DomainException(
		    "error.accounting.AccountingTransaction.cannot.reimburse.events.that.may.open");
	}

	if (!getToAccountEntry().canApplyReimbursement(amountToReimburse)) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.AccountingTransaction.amount.to.reimburse.exceeds.entry.amount",
		    getToAccountEntry().getDescription());
	}

	final AccountingTransaction transaction = new AccountingTransaction(responsibleUser, new Entry(
		EntryType.ADJUSTMENT, amountToReimburse.negate(), getToAccount()), new Entry(
		EntryType.ADJUSTMENT, amountToReimburse, getFromAccount()),
		new AccountingTransactionDetail(new DateTime(), paymentMode), this);

	getEvent().recalculateState(getWhenRegistered());

	return transaction;
    }

    public DateTime getWhenRegistered() {
	return getTransactionDetail().getWhenRegistered();

    }

    public DateTime getWhenProcessed() {
	return getTransactionDetail().getWhenProcessed();
    }

    public boolean isPayed(final int civilYear) {
	return getWhenRegistered().getYear() == civilYear;
    }

    public boolean isAdjustingTransaction() {
	return hasAdjustedTransaction();
    }

    public boolean hasBeenAdjusted() {
	return hasAnyAdjustmentTransactions();
    }

    public Entry getEntryFor(final Account account) {
	for (final Entry accountingEntry : getEntriesSet()) {
	    if (accountingEntry.getAccount() == account) {
		return accountingEntry;
	    }
	}

	throw new DomainException(
		"error.accounting.accountingTransaction.transaction.data.is.corrupted.because.no.entry.belongs.to.account");
    }

    private boolean canApplyReimbursement(final Money amount) {
	final Money extraAmount = getEvent().getExtraPayedAmount();
	return extraAmount.greaterOrEqualThan(amount);
    }

    public boolean isSourceAccountFromParty(Party party) {
	return getFromAccount().getParty() == party;
    }

}
