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

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.DomainExceptionWithLabelFormatter;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

public class Entry extends Entry_Base {

    public static Comparator<Entry> COMPARATOR_BY_MOST_RECENT_WHEN_REGISTERED = new Comparator<Entry>() {
        @Override
        public int compare(Entry leftEntry, Entry rightEntry) {
            int comparationResult = leftEntry.getWhenRegistered().compareTo(rightEntry.getWhenRegistered());
            return (comparationResult == 0) ? leftEntry.getExternalId().compareTo(rightEntry.getExternalId()) : comparationResult;
        }
    };

    private Entry() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    Entry(EntryType entryType, Money amount, Account account) {
        this();
        init(entryType, amount, account);
    }

    private void init(EntryType entryType, Money amount, Account account) {
        checkParameters(entryType, amount, account);
        super.setEntryType(entryType);
        super.setOriginalAmount(amount);
        super.setAccount(account);
    }

    private void checkParameters(EntryType entryType, Money amount, Account account) {
        if (entryType == null) {
            throw new DomainException("error.accounting.entry.entryType.cannot.be.null");
        }
        if (amount == null) {
            throw new DomainException("error.accounting.entry.originalAmount.cannot.be.null");
        }
        if (account == null) {
            throw new DomainException("error.accounting.entry.account.cannot.be.null");
        }
    }

    public boolean isPositiveAmount() {
        return getOriginalAmount().isPositive();
    }

    public boolean isAmountWithAdjustmentPositive() {
        return getAmountWithAdjustment().isPositive();
    }

    @Override
    public void setAccountingTransaction(AccountingTransaction accountingTransaction) {
        throw new DomainException("error.accounting.entry.cannot.modify.accountingTransaction");
    }

    @Override
    public void setOriginalAmount(Money amount) {
        throw new DomainException("error.accounting.entry.cannot.modify.originalAmount");
    }

    @Override
    public void setEntryType(EntryType entryType) {
        throw new DomainException("error.accounting.entry.cannot.modify.entryType");
    }

    public DateTime getWhenRegistered() {
        return getAccountingTransaction().getWhenRegistered();
    }

    public DateTime getWhenProcessed() {
        return getAccountingTransaction().getWhenProcessed();
    }

    @Override
    public void addReceipts(Receipt receipt) {
        throw new DomainException("error.accounting.Entry.cannot.add.receipt");
    }

    @Override
    public Set<Receipt> getReceiptsSet() {
        return Collections.unmodifiableSet(super.getReceiptsSet());
    }

    @Override
    public void removeReceipts(Receipt receipt) {
        throw new DomainException("error.org.fenixedu.academic.domain.accounting.Entry.cannot.remove.receipt");
    }

    public void setActiveReceipt(Receipt receipt) {
        if (getAdjustmentCreditNoteEntry() != null) {
            throw new DomainException("error.accounting.entry.is.already.associated.to.payed.creditNote");
        }

        if (isAssociatedToAnyActiveReceipt()) {
            throw new DomainExceptionWithLabelFormatter(
                    "error.Entry.cannot.be.associated.to.receipt.because.is.already.associated.to.another.active.receipt",
                    getDescription());
        }

        super.addReceipts(receipt);
    }

    public Receipt getActiveReceipt() {
        for (final Receipt receipt : getReceiptsSet()) {
            if (receipt.isActive()) {
                return receipt;
            }
        }

        return null;
    }

    public LabelFormatter getDescription() {
        return getAccountingTransaction().getDescriptionForEntryType(getEntryType());
    }

    public Money getAmountWithAdjustment() {
        return hasBeenAdjusted() ? getOriginalAmount().add(getTotalAdjustedAmount()) : getOriginalAmount();
    }

    private Money getTotalAdjustedAmount() {
        Money result = Money.ZERO;
        for (final AccountingTransaction transaction : getAccountingTransaction().getAdjustmentTransactionsSet()) {
            result = result.add(transaction.getEntryFor(getAccount()).getOriginalAmount());
        }

        return result;
    }

    public boolean isAdjusting() {
        return getAccountingTransaction().isAdjustingTransaction();
    }

    public boolean hasBeenAdjusted() {
        return getAccountingTransaction().hasBeenAdjusted();
    }

    private boolean canApplyAdjustment(final Money amountToAdjust) {
        return isAdjustmentAppliable() && getAmountWithAdjustment().add(amountToAdjust).greaterOrEqualThan(Money.ZERO);
    }

    private boolean isAdjustmentAppliable() {
        return getPersonFromEvent() == getFromAccountOwner();
    }

    public boolean isReimbursementAppliable() {
        return isAdjustmentAppliable() && getAmountWithAdjustment().isPositive();
    }

    private Person getPersonFromEvent() {
        return getAccountingTransaction().getEvent().getPerson();
    }

    public Party getFromAccountOwner() {
        return getAccountingTransaction().getFromAccount().getParty();
    }

    public boolean canApplyReimbursement(final Money amountToReimburse) {
        return canApplyAdjustment(amountToReimburse.negate());
    }

    protected boolean isAssociatedToAnyActiveReceipt() {
        for (final Receipt receipt : getReceiptsSet()) {
            if (receipt.isActive()) {
                return true;
            }
        }

        return false;
    }

    void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        super.setAccount(null);
        super.setAccountingTransaction(null);
        setRootDomainObject(null);

        super.deleteDomainObject();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!getReceiptsSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.accounting.Entry.belongs.to.receipt"));
        }
    }

    public PaymentMode getPaymentMode() {
        return getAccountingTransaction().getTransactionDetail().getPaymentMode();
    }

}
