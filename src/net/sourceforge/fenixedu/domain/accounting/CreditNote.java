package net.sourceforge.fenixedu.domain.accounting;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.CreditNoteEntryDTO;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class CreditNote extends CreditNote_Base {

    public static Comparator<CreditNote> COMPARATOR_BY_YEAR_AND_NUMBER = new Comparator<CreditNote>() {
	public int compare(CreditNote creditNote, CreditNote otherCreditNote) {
	    Integer yearComparationResult = creditNote.getYear().compareTo(otherCreditNote.getYear());
	    if (yearComparationResult == 0) {
		return creditNote.getNumber().compareTo(otherCreditNote.getNumber());
	    }
	    return yearComparationResult;
	}
    };

    private CreditNote() {
	super();
	super.setNumber(generateCreditNoteNumber());
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setWhenCreated(new DateTime());
	super.setYear(new DateTime().getYear());
    }

    private CreditNote(final Receipt receipt, final Employee employee) {
	this();
	init(receipt, employee);
    }

    private void init(Receipt receipt, Employee employee) {
	checkParameters(receipt, employee);
	checkRulesToCreate(receipt);
	super.setReceipt(receipt);
	internalChangeState(employee, CreditNoteState.EMITTED);
    }

    private void checkRulesToCreate(Receipt receipt) {
	if (receipt.isCancelled()){
	    throw new DomainException(
		    "error.accounting.CreditNote.cannot.be.created.for.cancelled.receipts");
	}
	
    }

    private void checkParameters(Receipt receipt, Employee employee) {
	if (receipt == null) {
	    throw new DomainException("error.accounting.CreditNote.receipt.cannot.be.null");
	}

	if (employee == null) {
	    throw new DomainException("error.accounting.CreditNote.employee.cannot.be.null");
	}
    }

    private Integer generateCreditNoteNumber() {
	final List<CreditNote> creditNotes = RootDomainObject.getInstance().getCreditNotes();
	return creditNotes.isEmpty() ? Integer.valueOf(1) : Collections.max(creditNotes,
		CreditNote.COMPARATOR_BY_YEAR_AND_NUMBER).getNumber() + 1;
    }

    @Override
    public void setReceipt(Receipt receipt) {
	throw new DomainException("error.accounting.CreditNote.cannot.modify.receipt");
    }

    @Override
    public void setEmployee(Employee employee) {
	throw new DomainException("error.accounting.CreditNote.cannot.modify.employee");
    }

    @Override
    public void setNumber(Integer number) {
	throw new DomainException("error.accounting.CreditNote.cannot.modify.number");
    }

    @Override
    public void setYear(Integer year) {
	throw new DomainException("error.accounting.CreditNote.cannot.modify.year");
    }

    @Override
    public void setWhenCreated(DateTime whenCreated) {
	throw new DomainException("error.accounting.CreditNote.cannot.modify.whenCreated");
    }

    @Override
    public void addCreditNoteEntries(CreditNoteEntry creditNoteEntry) {
	throw new DomainException("error.accounting.CreditNote.cannot.add.creditNoteEntry");
    }

    @Override
    public List<CreditNoteEntry> getCreditNoteEntries() {
	return Collections.unmodifiableList(super.getCreditNoteEntries());
    }

    @Override
    public Set<CreditNoteEntry> getCreditNoteEntriesSet() {
	return Collections.unmodifiableSet(super.getCreditNoteEntriesSet());
    }

    @Override
    public Iterator<CreditNoteEntry> getCreditNoteEntriesIterator() {
	return getCreditNoteEntriesSet().iterator();
    }

    @Override
    public void removeCreditNoteEntries(CreditNoteEntry creditNoteEntry) {
	throw new DomainException("error.accounting.CreditNote.cannot.remove.creditNoteEntry");
    }

    @Override
    public void setWhenUpdated(DateTime whenUpdated) {
	throw new DomainException("error.accounting.CreditNote.cannot.modify.whenUpdated");
    }

    @Override
    public void setState(CreditNoteState state) {
	throw new DomainException("error.accounting.CreditNote.cannot.modify.state");
    }

    private void internalChangeState(final Employee employee, CreditNoteState state) {
	super.setWhenUpdated(new DateTime());

	if (getState() != null && getState() != CreditNoteState.EMITTED) {
	    throw new DomainException(
		    "error.accounting.CreditNote.only.emitted.credit.notes.can.be.changed");
	}

	super.setState(state);
	super.setEmployee(employee);
    }

    public void confirm(final Employee employee, final PaymentMode paymentMode) {
	internalChangeState(employee, CreditNoteState.PAYED);

	for (final CreditNoteEntry creditNoteEntry : getCreditNoteEntriesSet()) {
	    creditNoteEntry.createAdjustmentAccountingEntry(employee.getPerson().getUser(), paymentMode);
	}

    }

    public void cancel(final Employee employee) {
	internalChangeState(employee, CreditNoteState.CANCELLED);
    }

    public void changeState(final Employee employee, final PaymentMode paymentMode,
	    final CreditNoteState state) {

	if (getState() == state) {
	    return;
	}

	if (state == CreditNoteState.CANCELLED) {
	    cancel(employee);
	} else if (state == CreditNoteState.PAYED) {
	    confirm(employee, paymentMode);
	} else {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.CreditNote.cannot.change.to.given.state");
	}

    }

    public static CreditNote create(Receipt receipt, Employee employee,
	    List<CreditNoteEntryDTO> entryDTOs) {
	final CreditNote creditNote = new CreditNote(receipt, employee);

	if (entryDTOs == null || entryDTOs.isEmpty()) {
	    throw new DomainException("error.accounting.CreditNote.cannot.be.created.without.entries");
	}

	for (final CreditNoteEntryDTO entryDTO : entryDTOs) {
	    if (!entryDTO.getEntry().canApplyReimbursement(entryDTO.getAmountToPay().negate())) {
		throw new DomainExceptionWithLabelFormatter(
			"error.accounting.CreditNoteEntry.amount.to.reimburse.exceeds.entry.amount",
			entryDTO.getEntry().getDescription());
	    }

	    new CreditNoteEntry(creditNote, entryDTO.getEntry(), entryDTO.getAmountToPay());

	}

	return creditNote;
    }

    public boolean isEmitted() {
	return getState() == CreditNoteState.EMITTED;
    }

    public boolean isCancelled() {
	return getState() == CreditNoteState.CANCELLED;
    }

    public boolean isPayed() {
	return getState() == CreditNoteState.PAYED;
    }

    public boolean isAllowedToChangeState() {
	return isEmitted();
    }

    public Money getTotalAmount() {
	Money totalAmount = Money.ZERO;

	for (final CreditNoteEntry creditNoteEntry : getCreditNoteEntriesSet()) {
	    totalAmount = totalAmount.add(creditNoteEntry.getAmount());
	}

	return totalAmount;
    }

}
