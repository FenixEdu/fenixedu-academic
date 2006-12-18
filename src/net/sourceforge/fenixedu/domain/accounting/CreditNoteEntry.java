package net.sourceforge.fenixedu.domain.accounting;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.util.Money;

public class CreditNoteEntry extends CreditNoteEntry_Base {

    CreditNoteEntry(final CreditNote creditNote, final Entry accountingEntry, final Money amount) {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	init(creditNote, accountingEntry, amount);
    }

    private void init(CreditNote creditNote, Entry accountingEntry, Money amount) {
	checkParameters(creditNote, accountingEntry, amount);
	checkRulesToCreate(accountingEntry, amount);
	super.setCreditNote(creditNote);
	super.setAccountingEntry(accountingEntry);
	super.setAmount(amount);

    }

    private void checkRulesToCreate(Entry accountingEntry, Money amount) {
	if (!accountingEntry.canApplyReimbursement(amount)) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.CreditNoteEntry.amount.to.reimburse.exceeds.entry.amount",
		    accountingEntry.getDescription());

	}
    }

    private void checkParameters(CreditNote creditNote, Entry accountingEntry, Money amount) {
	if (creditNote == null) {
	    throw new DomainException("error.accounting.CreditNoteEntry.creditNote.cannot.be.null");
	}

	if (accountingEntry == null) {
	    throw new DomainException("error.accounting.CreditNoteEntry.accountingEntry.cannot.be.null");
	}

	if (amount == null) {
	    throw new DomainException("error.accounting.CreditNoteEntry.amount.cannot.be.null");
	}

	if (amount.lessOrEqualThan(Money.ZERO)) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.CreditNoteEntry.amount.to.reimburse.must.be.greater.than.zero",
		    accountingEntry.getDescription());
	}

    }

    @Override
    public void setAccountingEntry(Entry accountingEntry) {
	throw new DomainException("error.accounting.CreditNoteEntry.cannot.modify.accountingEntry");
    }

    @Override
    public void setCreditNote(CreditNote creditNote) {
	throw new DomainException("error.accounting.enclosing_type.cannot.modify.creditNote");
    }

    @Override
    public void setAdjustmentAccountingEntry(Entry adjustmentAccountingEntry) {
	throw new DomainException(
		"error.accounting.CreditNoteEntry.cannot.modify.adjustmentAccountingEntry");
    }

    @Override
    public void setAmount(Money amount) {
	throw new DomainException("error.accounting.CreditNoteEntry.cannot.modify.amount");
    }

    void createAdjustmentAccountingEntry(final User responsibleUser, final PaymentMode paymentMode) {
	final AccountingTransaction transaction = getAccountingEntry().getAccountingTransaction()
		.reimburse(responsibleUser, paymentMode, getAmount());

	super.setAdjustmentAccountingEntry(transaction.getToAccountEntry());

    }

}
