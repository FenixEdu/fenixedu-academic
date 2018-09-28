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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.DomainExceptionWithLabelFormatter;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

/**
 * Two-ledged accounting transaction
 * 
 * @author naat
 * 
 */
public class AccountingTransaction extends AccountingTransaction_Base {

    public static final String SIGNAL_ANNUL = AccountingTransaction.class.getName() + ".annul";

    public static Comparator<AccountingTransaction> COMPARATOR_BY_WHEN_REGISTERED =
            (leftAccountingTransaction, rightAccountingTransaction) -> {
                int comparationResult =
                        leftAccountingTransaction.getWhenRegistered().compareTo(rightAccountingTransaction.getWhenRegistered());
                return (comparationResult == 0) ? leftAccountingTransaction.getExternalId().compareTo(
                        rightAccountingTransaction.getExternalId()) : comparationResult;
            };

    protected AccountingTransaction() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
    }

    public AccountingTransaction(User responsibleUser, Event event, Entry debit, Entry credit,
            AccountingTransactionDetail transactionDetail) {
        this();
        init(responsibleUser, event, debit, credit, transactionDetail);
    }

    private AccountingTransaction(User responsibleUser, Entry debit, Entry credit, AccountingTransactionDetail transactionDetail,
            AccountingTransaction transactionToAdjust) {
        this();
        init(responsibleUser, transactionToAdjust.getEvent(), debit, credit, transactionDetail, transactionToAdjust);
    }

    protected void init(User responsibleUser, Event event, Entry debit, Entry credit,
            AccountingTransactionDetail transactionDetail) {
        init(responsibleUser, event, debit, credit, transactionDetail, null);
    }

    protected void init(User responsibleUser, Event event, Entry debit, Entry credit,
            AccountingTransactionDetail transactionDetail, AccountingTransaction transactionToAdjust) {

        checkParameters(event, debit, credit);

        // check for operations after registered date
        List<String> operationsAfter = event.getOperationsAfter(transactionDetail.getWhenProcessed());

        if (!operationsAfter.isEmpty()) {
            throw new DomainException("error.accounting.AccountingTransaction.cannot.create.transaction",
                    String.join(",", operationsAfter));
        }

        super.setEvent(event);
        super.setResponsibleUser(responsibleUser);
        super.addEntries(debit);
        super.addEntries(credit);
        super.setAdjustedTransaction(transactionToAdjust);

        super.setTransactionDetail(transactionDetail);
    }

    private void checkParameters(Event event, Entry debit, Entry credit) {
        if (event == null) {
            throw new DomainException("error.accounting.accountingTransaction.event.cannot.be.null");
        }
        if (debit == null) {
            throw new DomainException("error.accounting.accountingTransaction.debit.cannot.be.null");
        }
        if (credit == null) {
            throw new DomainException("error.accounting.accountingTransaction.credit.cannot.be.null");
        }
    }

    @Override
    public void addEntries(Entry entries) {
        throw new DomainException("error.accounting.accountingTransaction.cannot.add.entries");
    }

    @Override
    public Set<Entry> getEntriesSet() {
        return Collections.unmodifiableSet(super.getEntriesSet());
    }

    @Override
    public void removeEntries(Entry entries) {
        throw new DomainException("error.accounting.accountingTransaction.cannot.remove.entries");
    }

    @Override
    public void setEvent(Event event) {
        super.setEvent(event);
    }

    @Override
    public void setResponsibleUser(User responsibleUser) {
        throw new DomainException("error.accounting.accountingTransaction.cannot.modify.responsibleUser");
    }

    @Override
    public void setAdjustedTransaction(AccountingTransaction adjustedTransaction) {
        throw new DomainException("error.accounting.accountingTransaction.cannot.modify.adjustedTransaction");
    }

    @Override
    public void setTransactionDetail(AccountingTransactionDetail transactionDetail) {
        throw new DomainException("error.accounting.AccountingTransaction.cannot.modify.transactionDetail");
    }

    @Override
    public void addAdjustmentTransactions(AccountingTransaction accountingTransaction) {
        throw new DomainException(
                "error.org.fenixedu.academic.domain.accounting.AccountingTransaction.cannot.add.accountingTransaction");
    }

    @Override
    public Set<AccountingTransaction> getAdjustmentTransactionsSet() {
        return Collections.unmodifiableSet(super.getAdjustmentTransactionsSet());
    }

    public Stream<AccountingTransaction> getAdjustmentTransactionStream() {
        return super.getAdjustmentTransactionsSet().stream();
    }

    @Override
    public void removeAdjustmentTransactions(AccountingTransaction adjustmentTransactions) {
        throw new DomainException(
                "error.org.fenixedu.academic.domain.accounting.AccountingTransaction.cannot.remove.accountingTransaction");
    }

    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        return getEvent().getDescriptionForEntryType(entryType);
    }

    public Account getFromAccount() {
        return getEntry(false).getAccount();

    }

    public Account getToAccount() {
        return getEntry(true).getAccount();

    }

    public Entry getToAccountEntry() {
        return getEntry(true);
    }

    public Entry getFromAccountEntry() {
        return getEntry(false);
    }

    private Entry getEntry(boolean positive) {
        for (final Entry entry : super.getEntriesSet()) {
            if (entry.isPositiveAmount() == positive) {
                return entry;
            }
        }

        throw new DomainException("error.accounting.accountingTransaction.transaction.data.is.corrupted");
    }

    public AccountingTransaction reimburse(User responsibleUser, PaymentMethod paymentMethod,String
            paymentReference,  Money amountToReimburse) {
        return reimburse(responsibleUser, paymentMethod,paymentReference, amountToReimburse, null);
    }

    public AccountingTransaction reimburse(User responsibleUser, PaymentMethod paymentMethod, String
            paymentReference, Money amountToReimburse, String comments) {
        return reimburse(responsibleUser, paymentMethod, paymentReference, amountToReimburse, comments, true);
    }

    public AccountingTransaction reimburse(User responsibleUser, PaymentMethod paymentMethod, String
            paymentReference, Money amountToReimburse,
            DateTime reimburseDate, String comments) {
        return reimburse(responsibleUser, paymentMethod, paymentReference, amountToReimburse, comments, true, reimburseDate);
    }

    public AccountingTransaction reimburseWithoutRules(User responsibleUser, PaymentMethod paymentMethod, String
            paymentReference, Money amountToReimburse) {
        return reimburseWithoutRules(responsibleUser, paymentMethod, paymentReference, amountToReimburse, null);
    }

    public AccountingTransaction reimburseWithoutRules(User responsibleUser, PaymentMethod paymentMethod, String
            paymentReference, Money amountToReimburse,
            String comments) {
        return reimburse(responsibleUser, paymentMethod, paymentReference, amountToReimburse, comments, false);
    }

    public void annul(final User responsibleUser, final String reason) {

        if (StringUtils.isEmpty(reason)) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.accounting.AccountingTransaction.cannot.annul.without.reason");
        }

        checkRulesToAnnul();

        annulReceipts();

        Signal.emit(SIGNAL_ANNUL, new DomainObjectEvent<AccountingTransaction>(this));

        reimburseWithoutRules(responsibleUser, getTransactionDetail().getPaymentMethod(), getTransactionDetail()
                .getPaymentReference(), getAmountWithAdjustment(), reason);
    }

    private void checkRulesToAnnul() {
        final List<String> operationsAfter = getEvent().getOperationsAfter(getWhenProcessed());
        if (!operationsAfter.isEmpty()) {
            throw new DomainException("error.accounting.AccountingTransaction.cannot.annul.operations.after",
                    String.join(",", operationsAfter));
        }
    }

    private void annulReceipts() {
        getToAccountEntry().getReceiptsSet().stream().filter(Receipt::isActive).forEach(r -> {
            Person responsible = Optional.ofNullable(Authenticate.getUser()).map(User::getPerson).orElse(null);
            r.annul(responsible);
        });
    }

    private AccountingTransaction reimburse(User responsibleUser, PaymentMethod paymentMethod, String paymentReference, Money
            amountToReimburse,
            String comments, boolean checkRules) {
        return reimburse(responsibleUser, paymentMethod, paymentReference, amountToReimburse, comments, checkRules, new DateTime
                ());

    }

    private AccountingTransaction reimburse(User responsibleUser, PaymentMethod paymentMethod, String paymentReference, Money
            amountToReimburse,
            String comments, boolean checkRules, DateTime reimburseDate) {

        if (checkRules && !canApplyReimbursement(amountToReimburse)) {
            throw new DomainException("error.accounting.AccountingTransaction.cannot.reimburse.events.that.may.open");
        }

        if (!getToAccountEntry().canApplyReimbursement(amountToReimburse)) {
            throw new DomainExceptionWithLabelFormatter(
                    "error.accounting.AccountingTransaction.amount.to.reimburse.exceeds.entry.amount", getToAccountEntry()
                            .getDescription());
        }

        final AccountingTransaction transaction =
                new AccountingTransaction(responsibleUser, new Entry(EntryType.ADJUSTMENT, amountToReimburse.negate(),
                        getToAccount()), new Entry(EntryType.ADJUSTMENT, amountToReimburse, getFromAccount()),
                        new AccountingTransactionDetail(reimburseDate, paymentMethod, paymentReference, comments), this);

        getEvent().recalculateState(new DateTime());

        return transaction;
    }

    public DateTime getWhenRegistered() {
        return getTransactionDetail().getWhenRegistered();
    }

    public DateTime getWhenProcessed() {
        return getTransactionDetail().getWhenProcessed();
    }

    public String getComments() {
        return getTransactionDetail().getComments();
    }

    public boolean isPayed(final int civilYear) {
        return getWhenRegistered().getYear() == civilYear;
    }

    public boolean isAdjustingTransaction() {
        return getAdjustedTransaction() != null;
    }

    public boolean hasBeenAdjusted() {
        return !super.getAdjustmentTransactionsSet().isEmpty();
    }

    public Entry getEntryFor(final Account account) {
        for (final Entry accountingEntry : super.getEntriesSet()) {
            if (accountingEntry.getAccount() == account) {
                return accountingEntry;
            }
        }

        throw new DomainException(
                "error.accounting.accountingTransaction.transaction.data.is.corrupted.because.no.entry.belongs.to.account");
    }

    private boolean canApplyReimbursement(final Money amount) {
        return getEvent().canApplyReimbursement(amount);
    }

    public boolean isSourceAccountFromParty(Party party) {
        return getFromAccount().getParty() == party;
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        blockers.addAll(getEvent().getOperationsAfter(getWhenProcessed()));
    }

    public void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        
        super.setAdjustedTransaction(null);
        for (; !getAdjustmentTransactionsSet().isEmpty(); getAdjustmentTransactionsSet().iterator().next().delete()) {
            ;
        }

        if (getTransactionDetail() != null) {
            getTransactionDetail().delete();
        }

        for (; !getEntriesSet().isEmpty(); getEntriesSet().iterator().next().delete()) {
            ;
        }

        super.setResponsibleUser(null);
        super.setEvent(null);
        setRootDomainObject(null);

        super.deleteDomainObject();
    }

    public Money getAmountWithAdjustment() {
        return getToAccountEntry().getAmountWithAdjustment();
    }

    public boolean isInsidePeriod(final YearMonthDay startDate, final YearMonthDay endDate) {
        return isInsidePeriod(startDate.toLocalDate(), endDate.toLocalDate());
    }

    public boolean isInsidePeriod(final LocalDate startDate, final LocalDate endDate) {
        return !getWhenRegistered().toLocalDate().isBefore(startDate) && !getWhenRegistered().toLocalDate().isAfter(endDate);
    }

    public boolean isInstallment() {
        return false;
    }

    public PaymentMethod getPaymentMethod() {
        return getTransactionDetail().getPaymentMethod();
    }

    public Money getOriginalAmount() {
        return getToAccountEntry().getOriginalAmount();
    }

}
