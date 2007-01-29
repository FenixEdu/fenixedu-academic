package net.sourceforge.fenixedu.domain.accounting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.CreditNoteEntryDTO;
import net.sourceforge.fenixedu.domain.Contributor;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class Receipt extends Receipt_Base {

    public static Comparator<Receipt> COMPARATOR_BY_YEAR_AND_NUMBER = new Comparator<Receipt>() {
	public int compare(Receipt receipt, Receipt otherReceipt) {
	    Integer yearComparationResult = receipt.getYear().compareTo(otherReceipt.getYear());
	    if (yearComparationResult == 0) {
		return receipt.getNumber().compareTo(otherReceipt.getNumber());
	    }
	    return yearComparationResult;
	}
    };

    public static Comparator<Receipt> COMPARATOR_BY_NUMBER = new Comparator<Receipt>() {
	public int compare(Receipt leftReceipt, Receipt rightReceipt) {
	    int comparationResult = leftReceipt.getNumber().compareTo(rightReceipt.getNumber());
	    return (comparationResult == 0) ? leftReceipt.getIdInternal().compareTo(
		    rightReceipt.getIdInternal()) : comparationResult;
	}
    };

    private Receipt() {
	super();

	// Generate number before adding to root domain object
	final int year = new DateTime().getYear();
	super.setNumber(generateReceiptNumber(year));
	super.setYear(year);
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setWhenCreated(new DateTime());
    }

    public Receipt(Employee employee, Person person, Party contributor, List<Entry> entries) {
	this();
	init(employee, person, contributor, entries);
    }

    private void init(Employee employee, Person person, Party contributor, List<Entry> entries) {
	checkParameters(employee, person, contributor, entries);
	checkRulesToCreate(entries);
	super.setPerson(person);
	super.setContributorParty(contributor);
	changeState(employee, ReceiptState.ACTIVE);

	for (final Entry entry : entries) {
	    entry.setActiveReceipt(this);
	}
    }

    private void checkRulesToCreate(List<Entry> entries) {
	final int year = getYear();
	for (final Entry entry : entries) {
	    if (entry.getWhenRegistered().getYear() != year) {
		throw new DomainException(
			"error.accounting.Receipt.entries.must.belong.to.receipt.civil.year");
	    }
	}
    }

    private void checkParameters(Employee employee, Person person, Party contributor, List<Entry> entries) {
	if (person == null) {
	    throw new DomainException("error.accouting.receipt.person.cannot.be.null");
	}
	if (contributor == null) {
	    throw new DomainException("error.accounting.receipt.contributor.cannot.be.null");
	}
	if (entries == null) {
	    throw new DomainException("error.accounting.receipt.entries.cannot.be.null");
	}
	if (employee == null) {
	    throw new DomainException("error.accounting.Receipt.employeee.cannot.be.null");
	}
	if (entries.isEmpty()) {
	    throw new DomainException("error.accounting.receipt.entries.cannot.be.empty");
	}

    }

    private void changeState(final Employee employee, final ReceiptState state) {
	super.setEmployee(employee);
	super.setState(state);
	super.setWhenUpdated(new DateTime());
    }

    @Override
    public void addEntries(Entry entries) {
	throw new DomainException("error.accounting.receipt.cannot.add.new.entries");
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
	throw new DomainException("error.accounting.receipt.cannot.remove.entries");
    }

    @Override
    public void setNumber(Integer number) {
	throw new DomainException("error.accounting.receipt.cannot.modify.number");
    }

    @Override
    public void removeReceiptsVersions(ReceiptPrintVersion receiptsVersions) {
	throw new DomainException("error.accounting.receipt.cannot.remove.receiptVersions");
    }

    @Override
    public void setPerson(Person person) {
	throw new DomainException("error.accounting.receipt.cannot.modify.person");
    }

    @Override
    public void setYear(Integer year) {
	throw new DomainException("error.accounting.receipt.cannot.modify.year");
    }

    @Override
    public void setWhenCreated(DateTime whenCreated) {
	throw new DomainException("error.accounting.receipt.cannot.modify.creation.date");
    }

    @Override
    public void addCreditNotes(CreditNote creditNote) {
	throw new DomainException("error.accounting.Receipt.cannot.add.creditNote");
    }
    
    @Override
    public void setEmployee(Employee employee) {
	throw new DomainException("error.accounting.receipt.cannot.modify.employee");
    }
    
    @Override
    public void setContributor(Contributor contributor) {
	throw new DomainException("error.accounting.receipt.cannot.modify.contributor");
    }
    
    @Override
    public void setContributorParty(Party contributorParty) {
	throw new DomainException("error.accounting.receipt.cannot.modify.contributorParty");
    }

    @Override
    public List<CreditNote> getCreditNotes() {
	return Collections.unmodifiableList(super.getCreditNotes());
    }

    @Override
    public Set<CreditNote> getCreditNotesSet() {
	return Collections.unmodifiableSet(super.getCreditNotesSet());
    }

    @Override
    public Iterator<CreditNote> getCreditNotesIterator() {
	return getCreditNotesSet().iterator();
    }

    @Override
    public void removeCreditNotes(CreditNote creditNote) {
	throw new DomainException("error.accounting.Receipt.cannot.remove.creditNote");
    }

    @Override
    public void setState(ReceiptState state) {
	throw new DomainException("error.accounting.Receipt.cannot.modify.state");
    }

    private Integer generateReceiptNumber(int year) {
	final List<Receipt> receipts = getReceiptsFor(year);
	return receipts.isEmpty() ? 1 : Collections.max(receipts, Receipt.COMPARATOR_BY_NUMBER)
		.getNumber() + 1;
    }

    public static List<Receipt> getReceiptsFor(int year) {
	final List<Receipt> result = new ArrayList<Receipt>();
	for (final Receipt receipt : RootDomainObject.getInstance().getReceipts()) {
	    if (receipt.getYear().intValue() == year) {
		result.add(receipt);
	    }
	}

	return result;
    }

    public ReceiptPrintVersion createReceiptVersion(Employee employee) {
	return new ReceiptPrintVersion(this, employee);
    }

    public ReceiptPrintVersion getMostRecentReceiptPrintVersion() {

	ReceiptPrintVersion result = null;
	for (final ReceiptPrintVersion receiptVersion : getReceiptsVersionsSet()) {
	    if (result == null || receiptVersion.getWhenCreated().isAfter(result.getWhenCreated())) {
		result = receiptVersion;
	    }
	}
	return result;
    }

    public Money getTotalAmount() {
	Money result = Money.ZERO;
	for (final Entry entry : getEntriesSet()) {
	    result = result.add(entry.getOriginalAmount());
	}
	return result;
    }

    public boolean isFromAdministrativeOffice(AdministrativeOffice administrativeOffice) {
	for (final Entry entry : getEntries()) {
	    if (!entry.getAccountingTransaction().getEvent().isPayableOnAdministrativeOffice(
		    administrativeOffice)) {
		return false;
	    }
	}
	return true;
    }

    public CreditNote createCreditNote(final Employee employee, final PaymentMode paymentMode,
	    final List<CreditNoteEntryDTO> creditNoteEntryDTOs) {

	return CreditNote.create(this, employee, creditNoteEntryDTOs);
    }

    public void cancel(final Employee employee) {
	checkRulesToCancel();
	changeState(employee, ReceiptState.CANCELLED);
    }

    private void checkRulesToCancel() {
	if (hasAnyCreditNotes()) {
	    throw new DomainException(
		    "error.accounting.Receipt.cannot.cancel.receipts.with.credit.notes");
	}

    }

    public boolean isActive() {
	return getState() == ReceiptState.ACTIVE;
    }

    public boolean isCancelled() {
	return getState() == ReceiptState.CANCELLED;
    }

    public Set<Entry> getReimbursableEntries() {
	final Set<Entry> result = new HashSet<Entry>();

	for (final Entry entry : getEntriesSet()) {
	    if (entry.isReimbursementAppliable()) {
		result.add(entry);
	    }
	}

	return result;
    }
    
    @Checked("RolePredicates.MANAGER_PREDICATE")
    public void delete() {
	
	if (!canBeDeleted()) {
	    throw new DomainException("error.accounting.Receipt.cannot.be.deleted");
	}
	
	for (; hasAnyReceiptsVersions() ; getReceiptsVersions().get(0).delete());
	
	super.setContributor(null);
	super.setContributorParty(null);
	super.setEmployee(null);
	super.getEntries().clear();
	super.setPerson(null);
	
	removeRootDomainObject();
	
	super.deleteDomainObject();
    }

    private boolean canBeDeleted() {
	return !hasAnyCreditNotes();
    }

}
