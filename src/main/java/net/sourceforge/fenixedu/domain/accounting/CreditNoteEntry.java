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

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;

public class CreditNoteEntry extends CreditNoteEntry_Base {

    public static Comparator<CreditNoteEntry> COMPARATOR_BY_WHEN_REGISTERED = new Comparator<CreditNoteEntry>() {
        @Override
        public int compare(CreditNoteEntry leftCreditNoteEntry, CreditNoteEntry rightCreditNoteEntry) {
            int comparationResult =
                    leftCreditNoteEntry.getAccountingEntry().getWhenRegistered()
                            .compareTo(rightCreditNoteEntry.getAccountingEntry().getWhenRegistered());
            return (comparationResult == 0) ? leftCreditNoteEntry.getExternalId().compareTo(rightCreditNoteEntry.getExternalId()) : comparationResult;
        }
    };

    CreditNoteEntry(final CreditNote creditNote, final Entry accountingEntry, final Money amount) {
        super();
        super.setRootDomainObject(Bennu.getInstance());
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
                    "error.accounting.CreditNoteEntry.amount.to.reimburse.exceeds.entry.amount", accountingEntry.getDescription());

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
        throw new DomainException("error.accounting.CreditNoteEntry.cannot.modify.adjustmentAccountingEntry");
    }

    @Override
    public void setAmount(Money amount) {
        throw new DomainException("error.accounting.CreditNoteEntry.cannot.modify.amount");
    }

    void createAdjustmentAccountingEntry(final User responsibleUser, final PaymentMode paymentMode) {
        final AccountingTransaction transaction =
                getAccountingEntry().getAccountingTransaction().reimburse(responsibleUser, paymentMode, getAmount());

        super.setAdjustmentAccountingEntry(transaction.getToAccountEntry());

    }

    @Deprecated
    public boolean hasCreditNote() {
        return getCreditNote() != null;
    }

    @Deprecated
    public boolean hasAdjustmentAccountingEntry() {
        return getAdjustmentAccountingEntry() != null;
    }

    @Deprecated
    public boolean hasAmount() {
        return getAmount() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasAccountingEntry() {
        return getAccountingEntry() != null;
    }

}
