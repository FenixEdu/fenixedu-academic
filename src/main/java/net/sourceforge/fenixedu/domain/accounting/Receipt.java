/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.accounting;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.CreditNoteEntryDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;
import net.sourceforge.fenixedu.util.Money;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class Receipt extends Receipt_Base {

    public static final String GENERIC_CONTRIBUTOR_PARTY_NUMBER = " ";

    private static final Integer MIN_YEAR_TO_CREATE_RECEIPTS = FenixConfigurationManager.getConfiguration()
            .getReceiptMinYearToCreate();

    private static final Map<Integer, String> NUMBER_SERIES_BY_YEAR = new HashMap<Integer, String>();

    static {

        final String[] parts = FenixConfigurationManager.getConfiguration().getReceiptNumberSeriesForYears().split(",");

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
        super.setRootDomainObject(Bennu.getInstance());
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
    public Set<CreditNote> getCreditNotesSet() {
        return Collections.unmodifiableSet(super.getCreditNotesSet());
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
        for (final Receipt receipt : Bennu.getInstance().getReceiptsSet()) {
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
        Collection<CreditNote> creditNotes = getCreditNotesSet();

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

    public void deleteReceiptPrintVersions() {
        check(this, RolePredicates.MANAGER_PREDICATE);
        for (; hasAnyReceiptsVersions(); getReceiptsVersions().iterator().next().delete()) {
            ;
        }
    }

    public void delete() {
        check(this, RolePredicates.MANAGER_PREDICATE);

        if (!canBeDeleted()) {
            throw new DomainException("error.accounting.Receipt.cannot.be.deleted");
        }

        deleteReceiptPrintVersions();

        super.setContributorParty(null);
        super.setResponsible(null);
        super.getEntriesSet().clear();
        super.setPerson(null);

        setRootDomainObject(null);

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

    public void changeContributor(final Party contributor) {
        check(this, RolePredicates.MANAGER_PREDICATE);
        super.setContributorParty(contributor);
    }

    public List<CreditNote> getEmittedCreditNotes() {
        final List<CreditNote> result = new ArrayList<CreditNote>();
        for (final CreditNote creditNote : super.getCreditNotesSet()) {
            if (creditNote.isEmitted()) {
                result.add(creditNote);
            }
        }

        return result;

    }

    public void edit(final Person responsible, final Party contributorParty, final String contributorName) {
        check(this, AcademicPredicates.MANAGE_STUDENT_PAYMENTS);
        markChange(responsible);
        checkContributorParameters(contributorParty, contributorName);
        super.setContributorParty(contributorParty);
        super.setContributorName(contributorName);
    }

    public void changeStateAndEntries(final ReceiptState state, final Set<Entry> newEntries) {
        check(this, RolePredicates.MANAGER_PREDICATE);

        super.setState(state);

        super.getEntriesSet().clear();

        for (final Entry entry : newEntries) {
            entry.setActiveReceipt(this);
        }

    }

    public boolean isSecondPrintVersion() {
        return getReceiptsVersionsSet().size() >= 1;
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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.CreditNote> getCreditNotes() {
        return getCreditNotesSet();
    }

    @Deprecated
    public boolean hasAnyCreditNotes() {
        return !getCreditNotesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.ReceiptPrintVersion> getReceiptsVersions() {
        return getReceiptsVersionsSet();
    }

    @Deprecated
    public boolean hasAnyReceiptsVersions() {
        return !getReceiptsVersionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.Entry> getEntries() {
        return getEntriesSet();
    }

    @Deprecated
    public boolean hasAnyEntries() {
        return !getEntriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.documents.ReceiptGeneratedDocument> getDocument() {
        return getDocumentSet();
    }

    @Deprecated
    public boolean hasAnyDocument() {
        return !getDocumentSet().isEmpty();
    }

    @Deprecated
    public boolean hasResponsible() {
        return getResponsible() != null;
    }

    @Deprecated
    public boolean hasYear() {
        return getYear() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasWhenUpdated() {
        return getWhenUpdated() != null;
    }

    @Deprecated
    public boolean hasReceiptDate() {
        return getReceiptDate() != null;
    }

    @Deprecated
    public boolean hasState() {
        return getState() != null;
    }

    @Deprecated
    public boolean hasContributorName() {
        return getContributorName() != null;
    }

    @Deprecated
    public boolean hasWhenCreated() {
        return getWhenCreated() != null;
    }

    @Deprecated
    public boolean hasNumber() {
        return getNumber() != null;
    }

    @Deprecated
    public boolean hasContributorParty() {
        return getContributorParty() != null;
    }

    @Deprecated
    public boolean hasOwnerUnit() {
        return getOwnerUnit() != null;
    }

    @Deprecated
    public boolean hasNumberSeries() {
        return getNumberSeries() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
