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

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

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
    public void setAccount(Account account) {
        throw new DomainException("error.accounting.entry.cannot.modify.account");
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
        throw new DomainException("error.net.sourceforge.fenixedu.domain.accounting.Entry.cannot.remove.receipt");
    }

    public void setActiveReceipt(Receipt receipt) {
        if (hasAdjustmentCreditNoteEntry()) {
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
        check(this, RolePredicates.MANAGER_PREDICATE);
        if (!canBeDeleted()) {
            throw new DomainException("error.accounting.Entry.belongs.to.receipt");
        }

        super.setAccount(null);
        super.setAccountingTransaction(null);
        setRootDomainObject(null);

        super.deleteDomainObject();
    }

    private boolean canBeDeleted() {
        return !hasAnyReceipts();
    }

    public PaymentMode getPaymentMode() {
        return getAccountingTransaction().getTransactionDetail().getPaymentMode();
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
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.Receipt> getReceipts() {
        return getReceiptsSet();
    }

    @Deprecated
    public boolean hasAnyReceipts() {
        return !getReceiptsSet().isEmpty();
    }

    @Deprecated
    public boolean hasOriginalAmount() {
        return getOriginalAmount() != null;
    }

    @Deprecated
    public boolean hasEntryType() {
        return getEntryType() != null;
    }

    @Deprecated
    public boolean hasAdjustmentCreditNoteEntry() {
        return getAdjustmentCreditNoteEntry() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasAccountingTransaction() {
        return getAccountingTransaction() != null;
    }

    @Deprecated
    public boolean hasAccount() {
        return getAccount() != null;
    }

}
