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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.CreditNoteEntryDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class CreditNote extends CreditNote_Base {

    public static Comparator<CreditNote> COMPARATOR_BY_NUMBER = new Comparator<CreditNote>() {
        @Override
        public int compare(CreditNote leftCreditNote, CreditNote rightCreditNote) {
            int comparationResult = leftCreditNote.getNumber().compareTo(rightCreditNote.getNumber());
            return (comparationResult == 0) ? leftCreditNote.getExternalId().compareTo(rightCreditNote.getExternalId()) : comparationResult;
        }
    };

    private CreditNote() {
        super();
        final Integer year = new DateTime().getYear();
        super.setNumber(generateCreditNoteNumber(year));
        super.setRootDomainObject(Bennu.getInstance());
        super.setWhenCreated(new DateTime());
        super.setYear(year);
    }

    private CreditNote(final Receipt receipt, final Person responsible) {
        this();
        init(receipt, responsible);
    }

    private void init(Receipt receipt, Person responsible) {
        checkParameters(receipt, responsible);
        checkRulesToCreate(receipt);
        super.setReceipt(receipt);
        internalChangeState(responsible, CreditNoteState.EMITTED);
    }

    private void checkRulesToCreate(Receipt receipt) {
        if (receipt.isAnnulled()) {
            throw new DomainException("error.accounting.CreditNote.cannot.be.created.for.annulled.receipts");
        }

    }

    private void checkParameters(Receipt receipt, Person responsible) {
        if (receipt == null) {
            throw new DomainException("error.accounting.CreditNote.receipt.cannot.be.null");
        }

        if (responsible == null) {
            throw new DomainException("error.accounting.CreditNote.responsible.cannot.be.null");
        }
    }

    private Integer generateCreditNoteNumber(final Integer year) {
        final List<CreditNote> creditNotes = getCreditNotesForYear(year);
        return creditNotes.isEmpty() ? Integer.valueOf(1) : Collections.max(creditNotes, CreditNote.COMPARATOR_BY_NUMBER)
                .getNumber() + 1;
    }

    private List<CreditNote> getCreditNotesForYear(final Integer year) {
        final List<CreditNote> result = new ArrayList<CreditNote>();
        for (final CreditNote creditNote : Bennu.getInstance().getCreditNotesSet()) {
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
    public void setResponsible(Person responsible) {
        throw new DomainException("error.accounting.CreditNote.cannot.modify.responsible");
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
    public Set<CreditNoteEntry> getCreditNoteEntriesSet() {
        return Collections.unmodifiableSet(super.getCreditNoteEntriesSet());
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

    private void internalChangeState(final Person responsible, CreditNoteState state) {
        super.setWhenUpdated(new DateTime());

        if (getState() != null && getState() != CreditNoteState.EMITTED) {
            throw new DomainException("error.accounting.CreditNote.only.emitted.credit.notes.can.be.changed");
        }

        super.setState(state);
        super.setResponsible(responsible);
    }

    public void confirm(final Person responsible, final PaymentMode paymentMode) {
        internalChangeState(responsible, CreditNoteState.PAYED);

        for (final CreditNoteEntry creditNoteEntry : getCreditNoteEntriesSet()) {
            creditNoteEntry.createAdjustmentAccountingEntry(responsible.getUser(), paymentMode);
        }

    }

    public void annul(final Person responsible) {
        internalChangeState(responsible, CreditNoteState.ANNULLED);
    }

    public void changeState(final Person responsible, final PaymentMode paymentMode, final CreditNoteState state) {

        if (getState() == state) {
            return;
        }

        if (state == CreditNoteState.ANNULLED) {
            annul(responsible);
        } else if (state == CreditNoteState.PAYED) {
            confirm(responsible, paymentMode);
        } else {
            throw new DomainException("error.net.sourceforge.fenixedu.domain.accounting.CreditNote.cannot.change.to.given.state");
        }

    }

    public static CreditNote create(Receipt receipt, Person responsible, List<CreditNoteEntryDTO> entryDTOs) {
        final CreditNote creditNote = new CreditNote(receipt, responsible);

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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.documents.CreditNoteGeneratedDocument> getDocument() {
        return getDocumentSet();
    }

    @Deprecated
    public boolean hasAnyDocument() {
        return !getDocumentSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.CreditNoteEntry> getCreditNoteEntries() {
        return getCreditNoteEntriesSet();
    }

    @Deprecated
    public boolean hasAnyCreditNoteEntries() {
        return !getCreditNoteEntriesSet().isEmpty();
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
    public boolean hasState() {
        return getState() != null;
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
    public boolean hasReceipt() {
        return getReceipt() != null;
    }

}
