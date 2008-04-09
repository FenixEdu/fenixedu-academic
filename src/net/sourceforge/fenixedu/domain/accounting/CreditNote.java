package net.sourceforge.fenixedu.domain.accounting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.dataTransferObject.accounting.CreditNoteEntryDTO;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;

public class CreditNote extends CreditNote_Base {

    public static Comparator<CreditNote> COMPARATOR_BY_NUMBER = new Comparator<CreditNote>() {
	public int compare(CreditNote leftCreditNote, CreditNote rightCreditNote) {
	    int comparationResult = leftCreditNote.getNumber().compareTo(rightCreditNote.getNumber());
	    return (comparationResult == 0) ? leftCreditNote.getIdInternal().compareTo(rightCreditNote.getIdInternal())
		    : comparationResult;
	}
    };

    private CreditNote() {
	super();
	final Integer year = new DateTime().getYear();
	super.setNumber(generateCreditNoteNumber(year));
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setWhenCreated(new DateTime());
	super.setYear(year);
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
	if (receipt.isAnnulled()) {
	    throw new DomainException("error.accounting.CreditNote.cannot.be.created.for.annulled.receipts");
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

    private Integer generateCreditNoteNumber(final Integer year) {
	final List<CreditNote> creditNotes = getCreditNotesForYear(year);
	return creditNotes.isEmpty() ? Integer.valueOf(1) : Collections.max(creditNotes, CreditNote.COMPARATOR_BY_NUMBER)
		.getNumber() + 1;
    }

    private List<CreditNote> getCreditNotesForYear(final Integer year) {
	final List<CreditNote> result = new ArrayList<CreditNote>();
	for (final CreditNote creditNote : RootDomainObject.getInstance().getCreditNotes()) {
	    if (creditNote.getYear().equals(year)) {
		result.add(creditNote);
	    }
	}

	return result;
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
	    throw new DomainException("error.accounting.CreditNote.only.emitted.credit.notes.can.be.changed");
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

    public void annul(final Employee employee) {
	internalChangeState(employee, CreditNoteState.ANNULLED);
    }

    public void changeState(final Employee employee, final PaymentMode paymentMode, final CreditNoteState state) {

	if (getState() == state) {
	    return;
	}

	if (state == CreditNoteState.ANNULLED) {
	    annul(employee);
	} else if (state == CreditNoteState.PAYED) {
	    confirm(employee, paymentMode);
	} else {
	    throw new DomainException("error.net.sourceforge.fenixedu.domain.accounting.CreditNote.cannot.change.to.given.state");
	}

    }

    public static CreditNote create(Receipt receipt, Employee employee, List<CreditNoteEntryDTO> entryDTOs) {
	final CreditNote creditNote = new CreditNote(receipt, employee);

	if (entryDTOs == null || entryDTOs.isEmpty()) {
	    throw new DomainException("error.accounting.CreditNote.cannot.be.created.without.entries");
	}

	for (final CreditNoteEntryDTO entryDTO : entryDTOs) {
	    if (!entryDTO.getEntry().canApplyReimbursement(entryDTO.getAmountToPay().negate())) {
		throw new DomainExceptionWithLabelFormatter(
			"error.accounting.CreditNoteEntry.amount.to.reimburse.exceeds.entry.amount", entryDTO.getEntry()
				.getDescription());
	    }

	    new CreditNoteEntry(creditNote, entryDTO.getEntry(), entryDTO.getAmountToPay());

	}

	checkIfEmittedCreditNotesExceedEventsMaxReimbursableAmounts(receipt);

	return creditNote;
    }

    private static void checkIfEmittedCreditNotesExceedEventsMaxReimbursableAmounts(Receipt receipt) {
	for (final Entry<Event, Money> each : calculateAmountsToReimburseByEvent(receipt).entrySet()) {
	    if (!each.getKey().canApplyReimbursement(each.getValue())) {
		throw new DomainExceptionWithLabelFormatter(
			"error.accounting.CreditNote.the.sum.credit.notes.in.emitted.state.exceeds.event.reimbursable.amount",
			each.getKey().getDescription(), new LabelFormatter(each.getKey().getReimbursableAmount().toPlainString()));
	    }
	}

    }

    private static Map<Event, Money> calculateAmountsToReimburseByEvent(Receipt receipt) {
	final Map<Event, Money> amountToReimburseByEvent = new HashMap<Event, Money>();
	for (final CreditNote creditNote : receipt.getEmittedCreditNotes()) {
	    for (final CreditNoteEntry creditNoteEntry : creditNote.getCreditNoteEntries()) {
		final Event event = creditNoteEntry.getAccountingEntry().getAccountingTransaction().getEvent();
		final Money amountToReimburse = creditNoteEntry.getAmount();
		if (amountToReimburseByEvent.containsKey(event)) {
		    amountToReimburseByEvent.put(event, amountToReimburseByEvent.get(event).add(amountToReimburse));
		} else {
		    amountToReimburseByEvent.put(event, amountToReimburse);
		}
	    }
	}
	return amountToReimburseByEvent;
    }

    public boolean isEmitted() {
	return getState() == CreditNoteState.EMITTED;
    }

    public boolean isAnnulled() {
	return getState() == CreditNoteState.ANNULLED;
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
