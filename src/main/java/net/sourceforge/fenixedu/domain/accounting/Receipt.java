package net.sourceforge.fenixedu.domain.accounting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.dataTransferObject.accounting.CreditNoteEntryDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.util.Money;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class Receipt extends Receipt_Base {

    public static final String GENERIC_CONTRIBUTOR_PARTY_NUMBER = " ";

    private static final Integer MIN_YEAR_TO_CREATE_RECEIPTS = PropertiesManager.getIntegerProperty("receipt.min.year.to.create");

    private static final Map<Integer, String> NUMBER_SERIES_BY_YEAR = new HashMap<Integer, String>();

    static {

        final String[] parts = PropertiesManager.getProperty("receipt.numberSeries.for.years").split(",");

        for (final String part : parts) {
            if (!StringUtils.isEmpty(part)) {
                final String[] yearAndSeries = part.split(":");
                NUMBER_SERIES_BY_YEAR.put(Integer.valueOf(yearAndSeries[0]), yearAndSeries[1]);
            }
        }

    }

    public static Comparator<Receipt> COMPARATOR_BY_NUMBER = new Comparator<Receipt>() {
        @Override
        public int compare(Receipt leftReceipt, Receipt rightReceipt) {
            int comparationResult = leftReceipt.getReceiptNumber().compareTo(rightReceipt.getReceiptNumber());
            return (comparationResult == 0) ? leftReceipt.getExternalId().compareTo(rightReceipt.getExternalId()) : comparationResult;
        }
    };

    protected Receipt() {
        super();
    }

    static public Receipt createWithContributorParty(Person responsible, Person person, Party contributor, Integer year,
            List<Entry> entries) {
        final Receipt result = new Receipt();
        result.init(responsible, person, contributor, null, year, entries, NUMBER_SERIES_BY_YEAR.get(year));

        return result;
    }

    static public Receipt createWithContributorName(Person responsible, Person person, String contributorName, Integer year,
            List<Entry> entries) {
        final Receipt result = new Receipt();
        result.init(responsible, person, null, contributorName, year, entries, NUMBER_SERIES_BY_YEAR.get(year));

        return result;
    }

    static public Receipt createWithContributorPartyOrContributorName(Person responsible, Person person, Party contributorParty,
            String contributorName, Integer year, List<Entry> entries) {
        final Receipt result = new Receipt();
        result.init(responsible, person, contributorParty, contributorName, year, entries, NUMBER_SERIES_BY_YEAR.get(year));

        return result;

    }

    private void init(Person responsible, Person person, Party contributorParty, String contributorName, Integer year,
            List<Entry> entries, String numberSeries) {

        checkParameters(person, contributorParty, contributorName, year, entries);

        checkRulesToCreate(person, year, entries);

        super.setNumber(generateReceiptNumber(year));
        super.setRootDomainObject(RootDomainObject.getInstance());
        super.setYear(year);
        super.setNumberSeries(numberSeries);
        super.setPerson(person);
        super.setContributorParty(contributorParty);
        super.setContributorName(contributorName);
        super.setWhenCreated(new DateTime());
        changeState(responsible, ReceiptState.ACTIVE);
        super.setReceiptDate(new YearMonthDay().getYear() != year.intValue() ? getLastDayOfYear(year) : new YearMonthDay());

        for (final Entry entry : entries) {
            entry.setActiveReceipt(this);
        }
    }

    private YearMonthDay getLastDayOfYear(Integer year) {
        return new YearMonthDay(year, 12, 31);
    }

    private void checkRulesToCreate(final Person person, final Integer year, final List<Entry> entries) {

        final YearMonthDay today = new YearMonthDay();

        if (year < MIN_YEAR_TO_CREATE_RECEIPTS.intValue()) {
            throw new DomainException("error.accounting.Receipt.invalid.receipt.year", MIN_YEAR_TO_CREATE_RECEIPTS.toString());
        }

        for (final Entry entry : entries) {
            if (entry.getWhenRegistered().getYear() != year) {
                throw new DomainException("error.accounting.Receipt.entries.must.belong.to.receipt.civil.year");
            }

            if (!entry.getAccountingTransaction().isSourceAccountFromParty(person)) {
                throw new DomainException("error.accounting.Receipt.entries.must.belong.to.person");
            }
        }
    }

    private void checkParameters(Person person, Party contributor, String contributorName, Integer year, List<Entry> entries) {

        if (person == null) {
            throw new DomainException("error.accouting.Receipt.person.cannot.be.null");
        }

        checkContributorParameters(contributor, contributorName);

        if (year == null) {
            throw new DomainException("error.accounting.Receipt.year.cannot.be.null");
        }

        if (entries == null) {
            throw new DomainException("error.accounting.Receipt.entries.cannot.be.null");
        }

        if (entries.isEmpty()) {
            throw new DomainException("error.accounting.Receipt.entries.cannot.be.empty");
        }

    }

    private void checkContributorParameters(Party contributor, String contributorName) {
        if (contributor == null && StringUtils.isEmpty(contributorName)) {
            throw new DomainException("error.accounting.Receipt.contributor.or.contributorName.must.be.not.null");
        }

        if (contributor != null && !StringUtils.isEmpty(contributorName)) {
            throw new DomainException("error.accounting.Receipt.contributor.and.contributorName.are.exclusive");
        }
    }

    private void changeState(final Person responsible, final ReceiptState state) {
        markChange(responsible);
        super.setState(state);
    }

    private void markChange(final Person responsible) {
        super.setWhenUpdated(new DateTime());
        super.setResponsible(responsible);
    }

    @Override
    public void addEntries(Entry entries) {
        throw new DomainException("error.accounting.Receipt.cannot.add.new.entries");
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
        throw new DomainException("error.accounting.Receipt.cannot.remove.entries");
    }

    @Override
    public void setNumber(Integer number) {
        throw new DomainException("error.accounting.Receipt.cannot.modify.number");
    }

    @Override
    public void removeReceiptsVersions(ReceiptPrintVersion receiptsVersions) {
        throw new DomainException("error.accounting.Receipt.cannot.remove.receiptVersions");
    }

    @Override
    public void setPerson(Person person) {
        throw new DomainException("error.accounting.Receipt.cannot.modify.person");
    }

    @Override
    public void setYear(Integer year) {
        throw new DomainException("error.accounting.Receipt.cannot.modify.year");
    }

    @Override
    public void setWhenCreated(DateTime whenCreated) {
        throw new DomainException("error.accounting.Receipt.cannot.modify.creation.date");
    }

    @Override
    public void addCreditNotes(CreditNote creditNote) {
        throw new DomainException("error.accounting.Receipt.cannot.add.creditNote");
    }

    @Override
    public void setResponsible(Person responsible) {
        throw new DomainException("error.accounting.Receipt.cannot.modify.responsible");
    }

    @Override
    public void setContributorParty(Party contributorParty) {
        throw new DomainException("error.accounting.Receipt.cannot.modify.contributorParty");
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
        throw new UnsupportedOperationException();
    }

    private Integer generateReceiptNumber(int year) {
        final List<Receipt> receipts = getReceiptsFor(year);
        return receipts.isEmpty() ? 1 : Collections.max(receipts, Receipt.COMPARATOR_BY_NUMBER).getReceiptNumber() + 1;
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

    public void registerReceiptPrint(Person person) {
        if (getState().isToRegisterPrint()) {
            new ReceiptPrintVersion(this, person);
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
            if (!entry.getAccountingTransaction().getEvent().isPayableOnAdministrativeOffice(administrativeOffice)) {
                return false;
            }
        }
        return true;
    }

    public CreditNote createCreditNote(final Person responsible, final PaymentMode paymentMode,
            final List<CreditNoteEntryDTO> creditNoteEntryDTOs) {

        return CreditNote.create(this, responsible, creditNoteEntryDTOs);
    }

    public void annul(final Person responsible) {
        checkRulesToAnnul();
        changeState(responsible, ReceiptState.ANNULLED);
    }

    private void checkRulesToAnnul() {
        if (hasAnyActiveCreditNotes()) {
            throw new DomainException("error.accounting.Receipt.cannot.annul.receipts.with.credit.notes");
        }

    }

    private boolean hasAnyActiveCreditNotes() {
        List<CreditNote> creditNotes = getCreditNotes();

        for (CreditNote creditNote : creditNotes) {
            if (!creditNote.isAnnulled()) {
                return true;
            }
        }

        return false;
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
        for (; hasAnyReceiptsVersions(); getReceiptsVersions().get(0).delete()) {
            ;
        }
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    public void delete() {

        if (!canBeDeleted()) {
            throw new DomainException("error.accounting.Receipt.cannot.be.deleted");
        }

        deleteReceiptPrintVersions();

        super.setContributorParty(null);
        super.setResponsible(null);
        super.getEntries().clear();
        super.setPerson(null);

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
        return !isNumberSeriesDefined() ? String.valueOf(super.getNumber()) : super.getNumber() + getNumberSeries();
    }

    @Override
    @Deprecated
    public Integer getNumber() {
        throw new DomainException("error.net.sourceforge.fenixedu.domain.accounting.Receipt.use.getNumberWithSeries.instead");
    }

    private Integer getReceiptNumber() {
        return super.getNumber();
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    public void changeContributor(final Party contributor) {
        super.setContributorParty(contributor);
    }

    public List<CreditNote> getEmittedCreditNotes() {
        final List<CreditNote> result = new ArrayList<CreditNote>();
        for (final CreditNote creditNote : super.getCreditNotes()) {
            if (creditNote.isEmitted()) {
                result.add(creditNote);
            }
        }

        return result;

    }

    @Checked("AcademicPredicates.MANAGE_STUDENT_PAYMENTS")
    public void edit(final Person responsible, final Party contributorParty, final String contributorName) {
        markChange(responsible);
        checkContributorParameters(contributorParty, contributorName);
        super.setContributorParty(contributorParty);
        super.setContributorName(contributorName);
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    public void changeStateAndEntries(final ReceiptState state, final Set<Entry> newEntries) {

        super.setState(state);

        super.getEntriesSet().clear();

        for (final Entry entry : newEntries) {
            entry.setActiveReceipt(this);
        }

    }

    public boolean isSecondPrintVersion() {
        return getReceiptsVersionsCount() >= 1;
    }

    static public Receipt readByYearAndNumber(final Integer year, final Integer number, final String series) {
        for (final Receipt receipt : Receipt.getReceiptsFor(year)) {
            if (receipt.getReceiptNumber().equals(number)) {
                if (series == null && receipt.getNumberSeries() == null) {
                    return receipt;
                } else if (series != null && receipt.getNumberSeries() != null && series.equals(receipt.getNumberSeries())) {
                    return receipt;
                }
            }
        }

        return null;
    }

}
