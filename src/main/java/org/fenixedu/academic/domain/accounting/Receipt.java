/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.accounting;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.dto.accounting.CreditNoteEntryDTO;
import org.fenixedu.academic.predicate.AcademicPredicates;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class Receipt extends Receipt_Base {

    public static final String GENERIC_CONTRIBUTOR_PARTY_NUMBER = " ";

    private static final Map<Integer, String> NUMBER_SERIES_BY_YEAR = new HashMap<Integer, String>();

    static {

        final String[] parts = FenixEduAcademicConfiguration.getConfiguration().getReceiptNumberSeriesForYears().split(",");

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

    static public Receipt create(Person responsible, Person person, String contributorName, String contributorNumber,
            String contributorAddress, Integer year, List<Entry> entries) {
        final Receipt result = new Receipt();
        result.init(responsible, person, contributorName, contributorNumber, contributorAddress, year, entries,
                NUMBER_SERIES_BY_YEAR.get(year));

        return result;

    }

    private void init(Person responsible, Person person, String contributorName, String contributorNumber,
            String contributorAddress, Integer year, List<Entry> entries, String numberSeries) {

        if (person == null) {
            throw new DomainException("error.accouting.Receipt.person.cannot.be.null");
        }

        if (StringUtils.isEmpty(contributorName)) {
            throw new DomainException("error.accounting.Receipt.contributor.or.contributorName.must.be.not.null");
        }

        if (year == null) {
            throw new DomainException("error.accounting.Receipt.year.cannot.be.null");
        }

        if (entries == null) {
            throw new DomainException("error.accounting.Receipt.entries.cannot.be.null");
        }

        if (entries.isEmpty()) {
            throw new DomainException("error.accounting.Receipt.entries.cannot.be.empty");
        }

        checkRulesToCreate(person, year, entries);

        super.setNumber(generateReceiptNumber(year));
        super.setRootDomainObject(Bennu.getInstance());
        super.setYear(year);
        super.setNumberSeries(numberSeries);
        super.setPerson(person);
        setContributorName(contributorName);
        setContributorNumber(contributorNumber);
        setContributorAddress(contributorAddress);
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

        if (year < FenixEduAcademicConfiguration.getConfiguration().getReceiptMinYearToCreate().intValue()) {
            throw new DomainException("error.accounting.Receipt.invalid.receipt.year", FenixEduAcademicConfiguration
                    .getConfiguration().getReceiptMinYearToCreate().toString());
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
        for (final Entry entry : getEntriesSet()) {
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
        for (; !getReceiptsVersionsSet().isEmpty(); getReceiptsVersionsSet().iterator().next().delete()) {
            ;
        }
    }

    public void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());

        deleteReceiptPrintVersions();

        super.setResponsible(null);
        super.getEntriesSet().clear();
        super.setPerson(null);

        setRootDomainObject(null);

        super.deleteDomainObject();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!getCreditNotesSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.accounting.Receipt.cannot.be.deleted"));
        }
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
        throw new DomainException("error.org.fenixedu.academic.domain.accounting.Receipt.use.getNumberWithSeries.instead");
    }

    private Integer getReceiptNumber() {
        return super.getNumber();
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

    public void edit(final Person responsible, final String contributorName, final String contributorNumber,
            final String contributorAddress) {
        check(this, AcademicPredicates.MANAGE_STUDENT_PAYMENTS);
        markChange(responsible);
        if (StringUtils.isEmpty(contributorName)) {
            throw new DomainException("error.accounting.Receipt.contributor.or.contributorName.must.be.not.null");
        }
        setContributorName(contributorName);
        setContributorNumber(contributorNumber);
        setContributorAddress(contributorAddress);
    }

    public void changeStateAndEntries(final ReceiptState state, final Set<Entry> newEntries) {
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

}
