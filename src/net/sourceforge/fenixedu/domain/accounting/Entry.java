package net.sourceforge.fenixedu.domain.accounting;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Money;
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

    Entry(EntryType entryType, Money amount, Account account) {
	this();
	init(entryType, amount, account);
    }

    private void init(EntryType entryType, Money amount, Account account) {
	checkParameters(entryType, amount, account);
	super.setEntryType(entryType);
	super.setOriginalAmount(amount);
	super.setAccount(account);
    }

    private void checkParameters(EntryType entryType, Money amount, Account account) {
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
	return getOriginalAmount().isPositive();
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
    public void setOriginalAmount(Money amount) {
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
    public void addReceipts(Receipt receipt) {
	throw new DomainException("error.accounting.Entry.cannot.add.receipt");
    }

    @Override
    public List<Receipt> getReceipts() {
	return Collections.unmodifiableList(super.getReceipts());
    }

    @Override
    public Set<Receipt> getReceiptsSet() {
	return Collections.unmodifiableSet(super.getReceiptsSet());
    }

    @Override
    public Iterator<Receipt> getReceiptsIterator() {
	return getReceiptsSet().iterator();
    }

    @Override
    public void removeReceipts(Receipt receipt) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.Entry.cannot.remove.receipt");
    }

    public void setActiveReceipt(Receipt receipt) {
	if (hasAdjustmentCreditNoteEntry()) {
	    throw new DomainException("error.accounting.entry.is.already.associated.to.payed.creditNote");
	}

	if (isAssociatedToAnyActiveReceipt()) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.Entry.cannot.be.associated.to.receipt.because.is.already.associated.to.another.active.receipt",
		    getDescription());
	}

	super.addReceipts(receipt);
    }

    public LabelFormatter getDescription() {
	return getAccountingTransaction().getDescriptionForEntryType(getEntryType());
    }

    public Money getAmountWithAdjustment() {
	return hasBeenAdjusted() ? getOriginalAmount().add(getTotalAdjustedAmount())
		: getOriginalAmount();
    }

    private Money getTotalAdjustedAmount() {
	Money result = Money.ZERO;
	for (final AccountingTransaction transaction : getAccountingTransaction()
		.getAdjustmentTransactionsSet()) {
	    result = result.add(transaction.getEntryFor(getAccount()).getOriginalAmount());
	}

	return result;
    }

    public boolean isAdjusting() {
	return getAccountingTransaction().isAdjustingTransaction();
    }

    public boolean hasBeenAdjusted() {
	return getAccountingTransaction().hasBeenAdjusted();
    }

    private boolean canApplyAdjustment(final Money amountToAdjust) {
	return isAdjustmentAppliable()
		&& getAmountWithAdjustment().add(amountToAdjust).greaterOrEqualThan(Money.ZERO);
    }

    private boolean isAdjustmentAppliable() {
	return getPersonFromEvent() == getFromAccountOwner();
    }

    public boolean isReimbursementAppliable() {
	return isAdjustmentAppliable();
    }

    private Person getPersonFromEvent() {
	return getAccountingTransaction().getEvent().getPerson();
    }

    public Party getFromAccountOwner() {
	return getAccountingTransaction().getFromAccount().getParty();
    }

    public boolean canApplyReimbursement(final Money amountToReimburse) {
	return canApplyAdjustment(amountToReimburse.negate());
    }

    protected boolean isAssociatedToAnyActiveReceipt() {
	for (final Receipt receipt : getReceiptsSet()) {
	    if (receipt.isActive()) {
		return true;
	    }
	}

	return false;
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    void delete() {
	if (!canBeDeleted()) {
	    throw new DomainException("error.accounting.Entry.belongs.to.receipt");
	}

	super.setAccount(null);
	super.setAccountingTransaction(null);
	removeRootDomainObject();

	super.deleteDomainObject();
    }

    private boolean canBeDeleted() {
	return !hasAnyReceipts();
    }

}
