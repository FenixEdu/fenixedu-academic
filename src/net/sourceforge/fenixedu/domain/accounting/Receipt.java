package net.sourceforge.fenixedu.domain.accounting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.CreditNoteEntryDTO;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Money;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

public class Receipt extends Receipt_Base {

    public static Comparator<Receipt> COMPARATOR_BY_NUMBER = new Comparator<Receipt>() {
	public int compare(Receipt leftReceipt, Receipt rightReceipt) {
	    int comparationResult = leftReceipt.getReceiptNumber().compareTo(
		    rightReceipt.getReceiptNumber());
	    return (comparationResult == 0) ? leftReceipt.getIdInternal().compareTo(
		    rightReceipt.getIdInternal()) : comparationResult;
	}
    };

    private Receipt() {
	this(new DateTime().getYear(), new DateTime(), null);
    }

    private Receipt(final Integer year, final DateTime whenCreated, final String numberSeries) {
	super();

	super.setNumber(generateReceiptNumber(year));
	super.setYear(year);
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setWhenCreated(whenCreated);
	super.setNumberSeries(numberSeries);
    }

    public Receipt(Employee employee, Person person, Party contributor, Unit creatorUnit,
	    Unit ownerUnit, List<Entry> entries) {
	this();
	init(employee, person, contributor, creatorUnit, ownerUnit, entries);
    }

    private void init(Employee employee, Person person, Party contributor, Unit creatorUnit,
	    Unit ownerUnit, List<Entry> entries) {
	checkParameters(employee, person, contributor, creatorUnit, ownerUnit, entries);
	checkRulesToCreate(person, entries);
	super.setPerson(person);
	super.setContributorParty(contributor);
	changeState(employee, ReceiptState.ACTIVE);
	super.setCreatorUnit(creatorUnit);
	super.setOwnerUnit(ownerUnit);

	for (final Entry entry : entries) {
	    entry.setActiveReceipt(this);
	}
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    public Receipt(final Employee employee, final Person person, final Party contributor,
	    final Unit creatorUnit, final Unit ownerUnit, final List<Entry> entries, final Integer year,
	    final String numberSeries, final DateTime whenCreated) {
	this(year, whenCreated, numberSeries);
	init(employee, person, contributor, creatorUnit, ownerUnit, entries, year, numberSeries,
		whenCreated);
    }

    private void init(Employee employee, Person person, Party contributor, Unit creatorUnit,
	    Unit ownerUnit, List<Entry> entries, Integer year, String numberSeries, DateTime whenCreated) {
	checkParameters(year, whenCreated, numberSeries);
	init(employee, person, contributor, creatorUnit, ownerUnit, entries);
    }

    private void checkParameters(Integer year, DateTime whenCreated, String numberSeries) {
	if (year == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.Receipt.year.cannot.be.null");
	}

	if (whenCreated == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.Receipt.whenCreated.cannot.be.null");
	}

	if (numberSeries == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.Receipt.numberSeries.cannot.be.null");
	}
    }

    private void checkRulesToCreate(final Person person, final List<Entry> entries) {
	final int year = getYear();
	for (final Entry entry : entries) {
	    if (entry.getWhenRegistered().getYear() != year) {
		throw new DomainException(
			"error.accounting.Receipt.entries.must.belong.to.receipt.civil.year");
	    }

	    if (!entry.getAccountingTransaction().isSourceAccountFromParty(person)) {
		throw new DomainException("error.accounting.Receipt.entries.must.belong.to.person");
	    }
	}
    }

    private void checkParameters(Employee employee, Person person, Party contributor, Unit creatorUnit,
	    Unit ownerUnit, List<Entry> entries) {
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
	if (creatorUnit == null) {
	    throw new DomainException("error.accounting.Receipt.creatorUnit.cannot.be.null");
	}
	if (ownerUnit == null) {
	    throw new DomainException("error.accounting.Receipt.ownerUnit.cannot.be.null");
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

    @Override
    public void setOwnerUnit(Unit ownerUnit) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.Receipt.cannot.modify.ownerUnit");
    }

    @Override
    public void setCreatorUnit(Unit creatorUnit) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.Receipt.cannot.modify.creatorUnit");
    }

    private Integer generateReceiptNumber(int year) {
	final List<Receipt> receipts = getReceiptsFor(year);
	return receipts.isEmpty() ? 1 : Collections.max(receipts, Receipt.COMPARATOR_BY_NUMBER)
		.getReceiptNumber() + 1;
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

    public void registerReceiptPrint(Employee employee) {
	if (getState().isToRegisterPrint()) {
	    new ReceiptPrintVersion(this, employee);
	}
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

    public void annul(final Employee employee) {
	checkRulesToAnnul();
	changeState(employee, ReceiptState.ANNULLED);
    }

    private void checkRulesToAnnul() {
	if (hasAnyCreditNotes()) {
	    throw new DomainException("error.accounting.Receipt.cannot.annul.receipts.with.credit.notes");
	}

    }

    public boolean isActive() {
	return getState() == ReceiptState.ACTIVE;
    }

    public boolean isAnnulled() {
	return getState() == ReceiptState.ANNULLED;
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
    public void deleteReceiptPrintVersions() {
	for (; hasAnyReceiptsVersions(); getReceiptsVersions().get(0).delete())
	    ;
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    public void delete() {

	if (!canBeDeleted()) {
	    throw new DomainException("error.accounting.Receipt.cannot.be.deleted");
	}

	deleteReceiptPrintVersions();

	super.setContributorParty(null);
	super.setEmployee(null);
	super.getEntries().clear();
	super.setPerson(null);
	super.setOwnerUnit(null);
	super.setCreatorUnit(null);

	removeRootDomainObject();

	super.deleteDomainObject();
    }

    private boolean canBeDeleted() {
	return !hasAnyCreditNotes();
    }

    public boolean isNumberSeriesDefined() {
	return !StringUtils.isEmpty(getNumberSeries());
    }

    public String getNumberWithSeries() {
	return !isNumberSeriesDefined() ? String.valueOf(super.getNumber()) : super.getNumber()
		+ getNumberSeries();
    }

    @Override
    @Deprecated
    public Integer getNumber() {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.Receipt.use.getNumberWithSeries.instead");
    }

    private Integer getReceiptNumber() {
	return super.getNumber();
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    public void changeContributor(final Party contributor) {
	super.setContributorParty(contributor);
    }
}
