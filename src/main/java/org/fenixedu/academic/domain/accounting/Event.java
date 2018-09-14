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

import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.calculator.DebtInterestCalculator;
import org.fenixedu.academic.domain.accounting.calculator.DebtInterestCalculator.Builder;
import org.fenixedu.academic.domain.accounting.events.PenaltyExemption;
import org.fenixedu.academic.domain.accounting.events.gratuity.exemption.penalty.FixedAmountPenaltyExemption;
import org.fenixedu.academic.domain.accounting.events.gratuity.exemption.penalty.InstallmentPenaltyExemption;
import org.fenixedu.academic.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import org.fenixedu.academic.domain.accounting.paymentCodes.EventPaymentCodeEntry;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.dto.accounting.AccountingTransactionDetailDTO;
import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.dto.accounting.SibsTransactionDetailDTO;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.signals.Signal;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Event extends Event_Base {

    public static final Comparator<Event> COMPARATOR_BY_DATE = (e1, e2) -> {
        final int i = e1.getWhenOccured().compareTo(e2.getWhenOccured());
        return i == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(e1, e2) : i;
    };

    protected Event() {
        super();

        super.setRootDomainObject(Bennu.getInstance());
        super.setWhenOccured(new DateTime());
        super.setCreatedBy(AccessControl.getPerson() != null ? AccessControl.getPerson().getUsername() : null);

        changeState(EventState.OPEN, new DateTime());
    }

    protected void init(EventType eventType, Person person) {
        checkParameters(eventType, person);
        super.setEventType(eventType);
        super.setParty(person);
    }

    protected void init(EventType eventType, Party party) {
        checkParameters(eventType, party);
        super.setEventType(eventType);
        super.setParty(party);
    }

    private void checkParameters(EventType eventType, Party person) throws DomainException {
        if (eventType == null) {
            throw new DomainException("error.accounting.Event.invalid.eventType");
        }
        if (person == null) {
            throw new DomainException("error.accounting.person.cannot.be.null");
        }
    }

    public boolean isOpen() {
        return (super.getEventState() == EventState.OPEN);
    }

    public boolean isInDebt() {
        return isOpen();
    }

    public boolean isClosed() {
        return (super.getEventState() == EventState.CLOSED);
    }

    public boolean isPayed() {
        return isClosed();
    }

    public boolean isCancelled() {
        return (super.getEventState() == EventState.CANCELLED);
    }

    public EventState getCurrentEventState() {
        return super.getEventState();
    }

    public boolean isInState(final EventState eventState) {
        return super.getEventState() == eventState;
    }

    public final Set<Entry> process(final User responsibleUser, final Collection<EntryDTO> entryDTOs, final AccountingTransactionDetailDTO transactionDetail) {
        if (entryDTOs.isEmpty()) {
            throw new DomainException("error.accounting.Event.process.requires.entries.to.be.processed");
        }

        checkConditionsToProcessEvent(transactionDetail);

        final Set<Entry> result = internalProcess(responsibleUser, entryDTOs, transactionDetail);

        recalculateState(new DateTime());

        return result;

    }

    public final Set<Entry> process(final User responsibleUser, final PaymentCode paymentCode, final Money amountToPay, final SibsTransactionDetailDTO transactionDetailDTO) {

        checkConditionsToProcessEvent(transactionDetailDTO);

        final Set<Entry> result = internalProcess(responsibleUser, paymentCode, amountToPay, transactionDetailDTO);

        recalculateState(new DateTime());

        return result;

    }

    private void checkConditionsToProcessEvent(final AccountingTransactionDetailDTO transactionDetail) {
        if (isClosed() && !isSibsTransaction(transactionDetail)) {
            throw new DomainException("error.accounting.Event.is.already.closed");
        }
    }

    private boolean isSibsTransaction(final AccountingTransactionDetailDTO transactionDetail) {
        return transactionDetail instanceof SibsTransactionDetailDTO;
    }

    protected Set<Entry> internalProcess(User responsibleUser, Collection<EntryDTO> entryDTOs, AccountingTransactionDetailDTO transactionDetail) {
        return getPostingRule().process(responsibleUser, entryDTOs, this, getFromAccount(), getToAccount(), transactionDetail);
    }

    protected Set<Entry> internalProcess(User responsibleUser, PaymentCode paymentCode, Money amountToPay, SibsTransactionDetailDTO transactionDetail) {
        return internalProcess(responsibleUser,
                Collections.singletonList(new EntryDTO(getEntryType(), this, amountToPay)), transactionDetail);
    }

    protected void closeEvent() {
        changeState(EventState.CLOSED, hasEventCloseDate() ? getEventCloseDate() : new DateTime());
    }

    public AccountingTransaction getLastNonAdjustingAccountingTransaction() {
        if (hasAnyNonAdjustingAccountingTransactions()) {
            return Collections.max(getNonAdjustingTransactions(), AccountingTransaction.COMPARATOR_BY_WHEN_REGISTERED);
        }

        return null;

    }

    @Override
    public void addAccountingTransactions(AccountingTransaction accountingTransactions) {
        throw new DomainException("error.accounting.Event.cannot.add.accountingTransactions");
    }

    @Override
    @Deprecated
    public Set<AccountingTransaction> getAccountingTransactionsSet() {
        //  Not really deprecated, but be advised, this method should be returning the code below, but framework says no.
        //  Can`t throw DomainException because it is called from the Event_Base.class. Should not be called from anywhere else.
        //        throw new DomainException(
        //                "error.accounting.Event.this.method.should.not.be.used.directly.use.getNonAdjustingTransactions.method.instead");
        return super.getAccountingTransactionsSet();
    }

    @Override
    public void removeAccountingTransactions(AccountingTransaction accountingTransactions) {
        throw new DomainException("error.accounting.Event.cannot.remove.accountingTransactions");
    }

    @Override
    public void setEventType(EventType eventType) {
        throw new DomainException("error.accounting.Event.cannot.modify.eventType");
    }

    @Override
    public void setWhenOccured(DateTime whenOccured) {
        throw new DomainException("error.accounting.Event.cannot.modify.occuredDateTime");
    }

    @Override
    public void setCreatedBy(String createdBy) {
        throw new DomainException("error.accounting.Event.cannot.modify.createdBy");
    }

    public void setPerson(Person person) {
        throw new DomainException("error.accounting.Event.cannot.modify.person");
    }

    @Override
    public void setEventState(EventState eventState) {
        throw new DomainException("error.accounting.Event.cannot.modify.eventState");
    }

    @Override
    public void setResponsibleForCancel(Person responsible) {
        throw new DomainException("error.accounting.Event.cannot.modify.employeeResponsibleForCancel");
    }

    protected boolean canCloseEvent(DateTime whenRegistered) {
        return calculateAmountToPay(whenRegistered).lessOrEqualThan(Money.ZERO);
    }

    public Set<Entry> getPositiveEntries() {
        return getNonAdjustingTransactions().stream()
                .filter(transaction -> transaction.getToAccountEntry().getAmountWithAdjustment().isPositive())
                .map(AccountingTransaction::getToAccountEntry)
                .collect(Collectors.toSet());
    }

    public Set<Entry> getEntriesWithoutReceipt() {
        return getNonAdjustingTransactions().stream()
                .filter(transaction -> transaction.isSourceAccountFromParty(getPerson()))
                .map(AccountingTransaction::getToAccountEntry)
                .filter(entry -> !entry.isAssociatedToAnyActiveReceipt() && entry.isAmountWithAdjustmentPositive())
                .collect(Collectors.toSet());
    }

    public List<AccountingTransaction> getNonAdjustingTransactions() {
        return super.getAccountingTransactionsSet().stream()
                .filter(transaction -> !transaction.isAdjustingTransaction())
                .filter(transaction -> transaction.getAmountWithAdjustment().isPositive())
                .collect(Collectors.toList());
    }

    public Stream<AccountingTransaction> getNonAdjustingTransactionStream() {
        return super.getAccountingTransactionsSet().stream()
            .filter(at -> !at.isAdjustingTransaction() && at.getAmountWithAdjustment().isPositive());
    }

    public List<AccountingTransaction> getAllAdjustedAccountingTransactions() {
        return super.getAccountingTransactionsSet().stream()
                .filter(AccountingTransaction::isAdjustingTransaction)
                .collect(Collectors.toList());
    }

    public List<AccountingTransaction> getAdjustedTransactions() {
        return super.getAccountingTransactionsSet().stream()
                .filter(transaction -> !transaction.isAdjustingTransaction())
                .collect(Collectors.toList());
    }

    public List<AccountingTransaction> getSortedNonAdjustingTransactions() {
        final List<AccountingTransaction> result = getNonAdjustingTransactions();
        result.sort(AccountingTransaction.COMPARATOR_BY_WHEN_REGISTERED);

        return result;
    }

    public boolean hasNonAdjustingAccountingTransactions(final AccountingTransaction accountingTransactions) {
        return getNonAdjustingTransactions().contains(accountingTransactions);
    }

    public boolean hasAnyNonAdjustingAccountingTransactions() {
        return !getNonAdjustingTransactions().isEmpty();
    }

    public boolean hasAnyPayments() {
        return hasAnyNonAdjustingAccountingTransactions();
    }

    public Money getPayedAmount() {
        return getPayedAmount(null);
    }

    public Money getPayedAmountFor(EntryType entryType) {
        if (isCancelled()) {
            throw new DomainException("error.accounting.Event.cannot.calculatePayedAmount.on.invalid.events");
        }

        Money payedAmount = Money.ZERO;
        for (final AccountingTransaction transaction : getNonAdjustingTransactions()) {
            if (transaction.getToAccountEntry().getEntryType().equals(entryType)) {
                payedAmount = payedAmount.add(transaction.getToAccountEntry().getAmountWithAdjustment());
            }
        }
        return payedAmount;
    }

    public Money getPayedAmount(final DateTime until) {
        if (isCancelled()) {
            throw new DomainException("error.accounting.Event.cannot.calculatePayedAmount.on.invalid.events");
        }

        Money payedAmount = Money.ZERO;
        for (final AccountingTransaction transaction : getNonAdjustingTransactions()) {
            if (until == null || !transaction.getWhenRegistered().isAfter(until)) {
                payedAmount = payedAmount.add(transaction.getToAccountEntry().getAmountWithAdjustment());
            }
        }

        return payedAmount;
    }

    public Money getPayedAmountBetween(final DateTime startDate, final DateTime endDate) {
        if (isCancelled()) {
            throw new DomainException("error.accounting.Event.cannot.calculatePayedAmountBetween.on.invalid.events");
        }

        Money payedAmount = Money.ZERO;
        for (final AccountingTransaction transaction : getNonAdjustingTransactions()) {
            if (!transaction.getWhenRegistered().isBefore(startDate) && !transaction.getWhenRegistered().isAfter(endDate)) {
                payedAmount = payedAmount.add(transaction.getToAccountEntry().getAmountWithAdjustment());
            }
        }

        return payedAmount;

    }

    private Money getPayedAmountUntil(final int civilYear) {
        if (isCancelled()) {
            throw new DomainException("error.accounting.Event.cannot.calculatePayedAmountUntil.on.invalid.events");
        }

        Money result = Money.ZERO;
        for (final AccountingTransaction transaction : getNonAdjustingTransactions()) {
            if (transaction.getWhenRegistered().getYear() <= civilYear) {
                result = result.add(transaction.getToAccountEntry().getAmountWithAdjustment());
            }
        }

        return result;
    }

    public Money getPayedAmountFor(final int civilYear) {
        if (isCancelled()) {
            throw new DomainException("error.accounting.Event.cannot.calculatePayedAmount.on.invalid.events");
        }

        Money amountForCivilYear = Money.ZERO;
        for (final AccountingTransaction accountingTransaction : getNonAdjustingTransactions()) {
            if (accountingTransaction.isPayed(civilYear)) {
                amountForCivilYear = amountForCivilYear.add(accountingTransaction.getToAccountEntry().getAmountWithAdjustment());
            }
        }

        return amountForCivilYear;

    }

    public Money getMaxDeductableAmountForLegalTaxes(final int civilYear) {
        if (isCancelled()) {
            throw new DomainException("error.accounting.Event.cannot.calculate.max.deductable.amount.for.legal.taxes.on.invalid.events");
        }

        if (isOpen() || !hasEventCloseDate()) {
            return calculatePayedAmountByPersonFor(civilYear);
        }

        final Money maxAmountForCivilYear = calculateTotalAmountToPay(getEventCloseDate()).subtract(getPayedAmountUntil(civilYear - 1)).subtract(calculatePayedAmountByOtherPartiesFor(civilYear));

        if (maxAmountForCivilYear.isPositive()) {
            final Money payedAmoutForPersonOnCivilYear = calculatePayedAmountByPersonFor(civilYear);

            return payedAmoutForPersonOnCivilYear.lessOrEqualThan(maxAmountForCivilYear) ? payedAmoutForPersonOnCivilYear : maxAmountForCivilYear;

        }

        return Money.ZERO;

    }

    private Money calculatePayedAmountByPersonFor(final int civilYear) {
        Money result = Money.ZERO;
        for (final AccountingTransaction transaction : getNonAdjustingTransactions()) {
            if (transaction.isPayed(civilYear) && transaction.isSourceAccountFromParty(getPerson())) {
                result = result.add(transaction.getToAccountEntry().getAmountWithAdjustment());
            }
        }

        return result;
    }

    public boolean hasPaymentsByPersonForCivilYear(final int civilYear) {
        return getNonAdjustingTransactions().stream()
                .anyMatch(transaction -> transaction.isSourceAccountFromParty(getPerson()) && transaction.isPayed(civilYear));
    }

    private Money calculatePayedAmountByOtherPartiesFor(final int civilYear) {
        Money result = Money.ZERO;
        for (final AccountingTransaction transaction : getNonAdjustingTransactions()) {
            if (transaction.isPayed(civilYear) && !transaction.isSourceAccountFromParty(getPerson())) {
                result = result.add(transaction.getToAccountEntry().getAmountWithAdjustment());
            }
        }

        return result;
    }

    public boolean hasPaymentsForCivilYear(final int civilYear) {
        return getNonAdjustingTransactions().stream().anyMatch(accountingTransaction -> accountingTransaction.isPayed(civilYear));
    }

    public final void recalculateState(final DateTime whenRegistered) {
        if (isCancelled()) {
            throw new DomainException("error.org.fenixedu.academic.domain.accounting.Event.cannot.recalculate.state.on.cancelled.events");
        }

        internalRecalculateState(whenRegistered.plusSeconds(1));
    }

    protected void internalRecalculateState(final DateTime whenRegistered) {
        final DateTime previousStateDate = getEventStateDate();
        final EventState previousState = super.getEventState();

        if (getEventState() == EventState.OPEN) {
            if (canCloseEvent(whenRegistered)) {
                closeNonProcessedCodes();
                closeEvent();
            }
        } else {
            if (!canCloseEvent(hasEventCloseDate() ? getEventCloseDate() : whenRegistered)) {
                changeState(EventState.OPEN, new DateTime());
                reopenCancelledCodes();
            }
        }

        // state does not change so keep previous date
        if (previousState == super.getEventState()) {
            super.setEventStateDate(previousStateDate);
        }

    }

    protected void reopenCancelledCodes() {
        for (final AccountingEventPaymentCode paymentCode : getCancelledPaymentCodes()) {
            paymentCode.setState(PaymentCodeState.NEW);
        }
    }

    protected void closeNonProcessedCodes() {
        for (final AccountingEventPaymentCode paymentCode : getNonProcessedPaymentCodes()) {
            paymentCode.setState(PaymentCodeState.CANCELLED);
        }
    }

    /**
     * Returns the total amount less the amount already paid. In other others
     * returns the debt due to this event (if positive)
     *
     * @param whenRegistered
     * @return
     */
    public Money calculateAmountToPay(DateTime whenRegistered) {
        final Money totalAmountToPay = calculateTotalAmountToPay(whenRegistered);

//        final Money remainingAmount = totalAmountToPay.subtract(getPayedAmount(whenRegistered));

        return totalAmountToPay.isPositive() ? totalAmountToPay : Money.ZERO;
    }

    public PaymentPlan getPaymentPlan() {
        return null;
    }


    /**
     * Should return entries representing the due date and the corresponding amount
     *
     */
    public final Map<LocalDate, Money> getDueDateAmountMap(DateTime when) {
        return getDueDateAmountMap(getPostingRule(), when);
    }

    public Map getDueDateAmountMap(PostingRule postingRule, DateTime when) {
        return Collections.singletonMap(getDueDateByPaymentCodes().toLocalDate(), postingRule.doCalculationForAmountToPay(this, when));
    }

    private Money calculateTotalAmountToPay(DateTime whenRegistered) {
        DebtInterestCalculator debtInterestCalculator = getDebtInterestCalculator(whenRegistered);
        return new Money(debtInterestCalculator.getTotalDueAmount());
    }

    public DebtInterestCalculator getDebtInterestCalculator(DateTime when) {
        return getDebtInterestCalculator(getPostingRule(), when);
    }

    public static BiFunction<AccountingTransaction, DateTime, Boolean> paymentsPredicate = (t, when) -> !t.getWhenProcessed().isAfter(when);

    protected DebtInterestCalculator getDebtInterestCalculator(PostingRule postingRule, DateTime when) {
        final Builder builder = new Builder(when);
        final Map<LocalDate, Money> dueDateAmountMap = getDueDateAmountMap(postingRule, when);
        final Money baseAmount = dueDateAmountMap.values().stream().reduce(Money.ZERO, Money::add);
        final Map<LocalDate, Money> dueDatePenaltyAmountMap = postingRule.getDueDatePenaltyAmountMap(this, when);
        final Set<LocalDate> debtInterestExemptions = getDebtInterestExemptions(when);
        final Set<LocalDate> debtFineExemptions = getDebtFineExemptions(when);
        

        dueDateAmountMap.forEach((date, amount) -> {
            builder.debt(getExternalId(), getWhenOccured(), date, getDescriptionForEntryType(postingRule.getEntryType()).toString(),
                    amount.getAmount(), debtInterestExemptions.contains(date), debtFineExemptions.contains(date));
        });

        dueDatePenaltyAmountMap.forEach((date, amount) -> {
            builder.fine(date, amount.getAmount());
        });

        getNonAdjustingTransactions().forEach(t -> {
            if (paymentsPredicate.apply(t, when)) {
                builder.payment(t.getExternalId(), t.getWhenProcessed(), t.getWhenRegistered().toLocalDate(), t
                        .getDescriptionForEntryType(postingRule.getEntryType()).toString(),
                        t.getAmountWithAdjustment().getAmount());
            }
        });

        getDebtExemptions().forEach(e -> {
            if (!e.getWhenCreated().isAfter(when)) {
                builder.debtExemption(e.getExternalId(),e.getWhenCreated(), e.getWhenCreated().toLocalDate(), e.getDescription
                        ().toString(),e.getExemptionAmount(baseAmount).getAmount());
            }
        });

        this.getDiscountsSet().forEach(d -> {
            if (!d.getWhenCreated().isAfter(when)) {
                builder.debtExemption(d.getExternalId(), d.getWhenCreated(), d.getWhenCreated().toLocalDate(),"Desconto",
                        d.getAmount().getAmount());
            }
        });

        getExemptionsSet()
            .stream()
            .filter(e -> !e.getWhenCreated().isAfter(when))
            .filter(FixedAmountPenaltyExemption.class::isInstance)
            .map(FixedAmountPenaltyExemption.class::cast)
            .forEach(e -> {
                if (e.isForInterest()) {
                    builder.interestExemption(e.getExternalId(),e.getWhenCreated(), e.getWhenCreated().toLocalDate(), e
                            .getDescription().toString(), e.getExemptionAmount(baseAmount).getAmount());
                }
                if (e.isForFine()) {
                    builder.fineExemption(e.getExternalId(),e.getWhenCreated(), e.getWhenCreated().toLocalDate(), e
                            .getDescription().toString(), e.getExemptionAmount(baseAmount).getAmount());
                }
            });

        builder.setToApplyInterest(FenixEduAcademicConfiguration.isToUseGlobalInterestRateTableForEventPenalties(this));
        return builder.build();
    }

    private Stream<Exemption> getDebtExemptions() {
        return this.getExemptionsSet().stream().filter(e -> !e.isPenaltyExemption());
    }

    private Set<LocalDate> getDebtFineExemptions(DateTime when) {
        return getExemptionsSet()
                   .stream()
                   .filter(e -> !e.getWhenCreated().isAfter(when))
                   .filter(PenaltyExemption.class::isInstance)
                   .map(PenaltyExemption.class::cast)
                   .flatMap(e -> e.getDueDates(when).stream())
                   .collect(Collectors.toSet());
    }

    private Set<LocalDate> getDebtInterestExemptions(DateTime when) {
        return getExemptionsSet()
            .stream()
            .filter(e -> !e.getWhenCreated().isAfter(when))
            .filter(InstallmentPenaltyExemption.class::isInstance)
            .map(InstallmentPenaltyExemption.class::cast)
            .map(i -> i.getInstallment().getEndDate(this))
            .collect(Collectors.toSet());
    }

    public Money getTotalAmount() {
        return getTotalAmount(new DateTime());
    }
    
    public Money getTotalAmount(DateTime when) {
        return new Money(getDebtInterestCalculator(when).getTotalAmount());
    }

    public Money getAmountToPay() {
        return calculateAmountToPay(new DateTime());
    }

    public Money getTotalAmountToPay(final DateTime whenRegistered) {
        return calculateTotalAmountToPay(whenRegistered);
    }

    public Money getTotalAmountToPay() {
        return getTotalAmountToPay(new DateTime());
    }

    public Money getOriginalAmountToPay() {
        return new Money(getDebtInterestCalculator(getWhenOccured().plusSeconds(1)).getDebtAmount());
    }

    public List<EntryDTO> calculateEntries() {
        return calculateEntries(new DateTime());
    }

    public List<EntryDTO> calculateEntries(DateTime when) {
        return getPostingRule().calculateEntries(this, when);
    }

    public void open() {

        changeState(EventState.OPEN, new DateTime());
        super.setResponsibleForCancel(null);
        super.setCancelJustification(null);

    }

    public void cancel(final Person responsible) {
        cancel(responsible, null);
    }

    public void forceCancel(final Person responsible, final String cancelJustification) {
        if (isCancelled()) {
            return;
        }
        changeState(EventState.CANCELLED, new DateTime());
        super.setResponsibleForCancel(responsible);
        super.setCancelJustification(cancelJustification);
        closeNonProcessedCodes();
    }

    public void cancel(final Person responsible, final String cancelJustification) {
        if (!isOpen()) {
            throw new DomainException("error.accounting.Event.only.open.events.can.be.cancelled");
        }

        if (getPayedAmount().isPositive()) {
            throw new DomainException("error.accounting.Event.cannot.cancel.events.with.payed.amount.greater.than.zero");
        }

        forceCancel(responsible, cancelJustification);
    }

    public void cancel(final String cancelJustification) {
        if (isCancelled()) {
            return;
        }

        if (getPayedAmount().isPositive()) {
            throw new DomainException("error.accounting.Event.cannot.cancel.events.with.payed.amount.greater.than.zero");
        }

        changeState(EventState.CANCELLED, new DateTime());
        super.setCancelJustification(cancelJustification);
        closeNonProcessedCodes();
    }

    public boolean hasInstallments() {
        return false;
    }

    public Optional<EventPaymentCodeEntry> getAvailablePaymentCodeEntry() {
        return getEventPaymentCodeEntrySet().stream()
                .filter(e -> e.getPaymentCode().isNew())
                .max(Comparator.comparing(EventPaymentCodeEntry::getCreated));
    }

    public EventPaymentCodeEntry calculatePaymentCodeEntry() {
        final Money amount = calculateAmountToPay(DateTime.now());
        return EventPaymentCodeEntry.getOrCreate(this, amount);
    }

    public List<AccountingEventPaymentCode> getNonProcessedPaymentCodes() {
        return super.getPaymentCodesSet().stream().filter(PaymentCode::isNew).collect(Collectors.toList());
    }

    public boolean hasNonProcessedPaymentCodes() {
        return super.getPaymentCodesSet().stream().anyMatch(PaymentCode::isNew);
    }

    public List<AccountingEventPaymentCode> getCancelledPaymentCodes() {

        return super.getPaymentCodesSet().stream().filter(PaymentCode::isCancelled).collect(Collectors.toList());
    }

    public Set<AccountingEventPaymentCode> getAllPaymentCodes() {
        return Collections.unmodifiableSet(super.getPaymentCodesSet());
    }

    @Override
    public void addPaymentCodes(AccountingEventPaymentCode paymentCode) {
        throw new DomainException("error.org.fenixedu.academic.domain.accounting.Event.cannot.add.paymentCode");
    }

    @Override
    @Deprecated
    public Set<AccountingEventPaymentCode> getPaymentCodesSet() {
        //  Not really deprecated, but be advised, this method should be returning the code below, but framework says no.
        //  Can`t throw DomainException because it is called from the Event_Base.class. Should not be called from anywhere else.
        //        throw new DomainException(
        //                "error.org.fenixedu.academic.domain.accounting.Event.paymentCodes.cannot.be.accessed.directly");
        return super.getPaymentCodesSet();
    }

    @Override
    public void removePaymentCodes(AccountingEventPaymentCode paymentCode) {
        throw new DomainException("error.org.fenixedu.academic.domain.accounting.Event.cannot.remove.paymentCode");
    }

    public static List<Event> readNotCancelled() {
        return Bennu.getInstance().getAccountingEventsSet().stream()
                .filter(event -> !event.isCancelled())
                .collect(Collectors.toList());

    }

    public PaymentCodeState getPaymentCodeStateFor(final PaymentMode paymentMode) {
        return (paymentMode == PaymentMode.ATM) ? PaymentCodeState.PROCESSED : PaymentCodeState.CANCELLED;
    }

    public LabelFormatter getDescription() {
        final LabelFormatter result = new LabelFormatter();
        result.appendLabel(getEventType().getQualifiedName(), Bundle.ENUMERATION);
        return result;
    }

    protected YearMonthDay calculateNextEndDate(final YearMonthDay yearMonthDay) {
        final YearMonthDay nextMonth = yearMonthDay.plusMonths(1);
        return new YearMonthDay(nextMonth.getYear(), nextMonth.getMonthOfYear(), 1).minusDays(1);
    }

    public Money getReimbursableAmount() {
//        if (!isClosed() || !hasEventCloseDate()) {
//            return Money.ZERO;
//        }

//        final Money extraPayedAmount = getPayedAmount().subtract(calculateTotalAmountToPay(getEventCloseDate()));

//        if (extraPayedAmount.isPositive()) {
//            final Money amountPayedByPerson = calculatePayedAmountByPerson();
//            return amountPayedByPerson.lessOrEqualThan(extraPayedAmount) ? amountPayedByPerson : extraPayedAmount;
//        }

//        return Money.ZERO;

        return calculatePayedAmountByPerson();

    }

    protected boolean hasEventCloseDate() {
        return getEventCloseDate() != null;
    }

    protected DateTime getEventCloseDate() {
        return getSortedNonAdjustingTransactions().stream()
                .filter(transaction -> canCloseEvent(transaction.getWhenRegistered()))
                .findFirst().map(AccountingTransaction::getWhenRegistered).orElse(null);

    }

    private Money calculatePayedAmountByPerson() {
        return getNonAdjustingTransactions().stream().filter(t -> t.isSourceAccountFromParty(getPerson())).map(t -> t
                .getToAccountEntry().getAmountWithAdjustment()).reduce(Money.ZERO, Money::add);
    }

    public final void forceChangeState(EventState state, DateTime when) {
        changeState(state, when);
    }

    protected void changeState(EventState state, DateTime when) {
        Signal.emit(EventState.EVENT_STATE_CHANGED, new EventState.ChangeStateEvent(state, this, when));
        super.setEventState(state);
        super.setEventStateDate(when);
    }


    public boolean isOtherPartiesPaymentsSupported() {
        return false;
    }

    public final void addOtherPartyAmount(User responsibleUser, Party party, Money amount, AccountingTransactionDetailDTO transactionDetailDTO) {

        getPostingRule().addOtherPartyAmount(responsibleUser, this, party.getAccountBy(AccountType.EXTERNAL), getToAccount(), amount, transactionDetailDTO);

        recalculateState(new DateTime());
    }

    public final AccountingTransaction depositAmount(final User responsibleUser, final Money amount, final AccountingTransactionDetailDTO transactionDetailDTO) {

        final AccountingTransaction result = getPostingRule().depositAmount(responsibleUser, this, getParty().getAccountBy(AccountType.EXTERNAL), getToAccount(), amount, transactionDetailDTO);

        recalculateState(new DateTime());

        return result;
    }

    public final AccountingTransaction depositAmount(final User responsibleUser, final Money amount, final EntryType entryType, final AccountingTransactionDetailDTO transactionDetailDTO) {

        final AccountingTransaction result =
            getPostingRule().depositAmount(responsibleUser, this, getParty().getAccountBy(AccountType.EXTERNAL), getToAccount(), amount, entryType, transactionDetailDTO);

        recalculateState(new DateTime());

        return result;
    }

    public Money calculateOtherPartiesPayedAmount() {
        Money result = Money.ZERO;
        for (final AccountingTransaction accountingTransaction : getNonAdjustingTransactions()) {
            if (!accountingTransaction.isSourceAccountFromParty(getParty())) {
                result = result.add(accountingTransaction.getToAccountEntry().getAmountWithAdjustment());
            }
        }

        return result;
    }

    public Set<Entry> getOtherPartyEntries() {
        return getNonAdjustingTransactions().stream()
                .filter(transaction -> !transaction.isSourceAccountFromParty(getPerson()))
                .map(AccountingTransaction::getToAccountEntry)
                .collect(Collectors.toSet());
    }

    public void rollbackCompletly() {
        while (!getNonAdjustingTransactions().isEmpty()) {
            getNonAdjustingTransactions().iterator().next().delete();
        }

        changeState(EventState.OPEN, new DateTime());

        for (final PaymentCode paymentCode : getExistingPaymentCodes()) {
            paymentCode.setState(PaymentCodeState.NEW);
        }
    }

    public Set<AccountingEventPaymentCode> getExistingPaymentCodes() {
        return Collections.unmodifiableSet(super.getPaymentCodesSet());
    }

    protected abstract Account getFromAccount();

    public abstract Account getToAccount();

    public abstract LabelFormatter getDescriptionForEntryType(EntryType entryType);

    abstract public PostingRule getPostingRule();

    final public void delete() {
        checkRulesToDelete();
        disconnect();
    }

    protected void disconnect() {
        while (!super.getPaymentCodesSet().isEmpty()) {
            super.getPaymentCodesSet().iterator().next().delete();
        }

        while (!getExemptionsSet().isEmpty()) {
            getExemptionsSet().iterator().next().delete(false);
        }

        super.setParty(null);
        super.setResponsibleForCancel(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public boolean canBeCanceled() {
        if (isClosed() || !getNonAdjustingTransactions().isEmpty() || !getDiscountsSet().isEmpty() ||
                !getExemptionsSet().isEmpty()) {
            return false;
        }
        return true;
    }
    
    protected void checkRulesToDelete() {
        if (isClosed() || !getNonAdjustingTransactions().isEmpty()) {
            throw new DomainException("error.accounting.Event.cannot.delete.because.event.is.already.closed.or.has.transactions.associated");

        }
    }

    public static List<Event> readBy(final EventType eventType) {
        return Bennu.getInstance().getAccountingEventsSet().stream()
                .filter(event -> event.getEventType() == eventType)
                .collect(Collectors.toList());

    }

    public static List<Event> readWithPaymentsByPersonForCivilYear(int civilYear) {
        return readNotCancelled().stream()
                .filter(event -> event.hasPaymentsByPersonForCivilYear(civilYear))
                .collect(Collectors.toList());

    }

    @Override
    public void addExemptions(Exemption exemption) {
        throw new DomainException("error.org.fenixedu.academic.domain.accounting.Event.cannot.add.exemption");
    }

    @Override
    public Set<Exemption> getExemptionsSet() {
        return Collections.unmodifiableSet(super.getExemptionsSet());
    }

    public Stream<Exemption> getExemptionsStream() {
        return super.getExemptionsSet().stream();
    }

    @Override
    public void removeExemptions(Exemption exemption) {
        throw new DomainException("error.org.fenixedu.academic.domain.accounting.Event.cannot.remove.exemption");
    }

    public List<PenaltyExemption> getPenaltyExemptions() {
        return getExemptionsSet().stream()
                .filter(exemption -> exemption instanceof PenaltyExemption)
                .map(exemption -> (PenaltyExemption) exemption)
                .collect(Collectors.toList());
    }

    public boolean hasAnyPenaltyExemptionsFor(Class type) {
        return getExemptionsSet().stream().anyMatch(exemption -> exemption.getClass().equals(type));
    }

    public List<PenaltyExemption> getPenaltyExemptionsFor(Class type) {
        return getExemptionsSet().stream()
                .filter(exemption -> exemption.getClass().equals(type))
                .map(exemption -> (PenaltyExemption) exemption)
                .collect(Collectors.toList());
    }

    public DateTime getLastPaymentDate() {
        final AccountingTransaction transaction = getLastNonAdjustingAccountingTransaction();
        return (transaction != null) ? transaction.getWhenRegistered() : null;
    }

    public boolean isLetterSent() {
        return getWhenSentLetter() != null;
    }

    public void markLetterSent() {
        setWhenSentLetter(new LocalDate());
    }

    public void transferPaymentsAndCancel(Person responsible, Event targetEvent, String justification) {

        checkConditionsToTransferPaymentsAndCancel(targetEvent);

        for (final Entry entryToTransfer : getPositiveEntries()) {

            final AccountingTransactionDetailDTO transactionDetail = createAccountingTransactionDetailForTransfer(entryToTransfer.getAccountingTransaction());

            targetEvent.depositAmount(responsible.getUser(), entryToTransfer.getAmountWithAdjustment(), transactionDetail);

            entryToTransfer.getAccountingTransaction().reimburseWithoutRules(responsible.getUser(), PaymentMode.CASH, entryToTransfer.getAmountWithAdjustment());
        }

        cancel(responsible, justification);

    }

    protected void checkConditionsToTransferPaymentsAndCancel(Event targetEvent) {
        if (getEventType() != targetEvent.getEventType()) {
            throw new DomainException("error.accounting.Event.events.must.be.compatible");
        }

        if (isCancelled()) {
            throw new DomainException("error.accounting.Event.cannot.transfer.payments.from.cancelled.events");
        }

        if (this == targetEvent) {
            throw new DomainException("error.org.fenixedu.academic.domain.accounting.Event.target.event.must.be.different.from.source");
        }
    }

    private AccountingTransactionDetailDTO createAccountingTransactionDetailForTransfer(final AccountingTransaction transaction) {
        final String comments = transaction.getEvent().getClass().getName() + ":" + transaction.getEvent().getExternalId() + "," + transaction.getClass().getName() + ":" + transaction.getExternalId();

        return new AccountingTransactionDetailDTO(transaction.getTransactionDetail().getWhenRegistered(), PaymentMode.CASH, comments);

    }

    public boolean isNotCancelled() {
        return !isCancelled();
    }

    public boolean isAnnual() {
        return false;
    }

    public boolean canApplyReimbursement(final Money amount) {
        return getReimbursableAmount().greaterOrEqualThan(amount);
    }

    public boolean isPayableOnAdministrativeOffice(AdministrativeOffice administrativeOffice) {
        return false;
    }

    public EntryType getEntryType() {
        return EntryType.GENERIC_EVENT;
    }

    public void addDiscount(final Person responsible, final Money amount) {
        new Discount(this, responsible, amount);
    }

    public Money getTotalDiscount() {
        Money result = Money.ZERO;
        for (final Discount discount : getDiscountsSet()) {
            result = result.add(discount.getAmount());
        }
        return result;
    }

    public boolean isPaymentPlanChangeAllowed() {
        return false;
    }

    public boolean isGratuity() {
        return false;
    }

    public boolean isAcademicServiceRequestEvent() {
        return false;
    }

    public boolean isIndividualCandidacyEvent() {
        return false;
    }

    public boolean isResidenceEvent() {
        return false;
    }

    public boolean isPhdEvent() {
        return false;
    }

    public boolean isDfaRegistrationEvent() {
        return false;
    }

    public boolean isEnrolmentOutOfPeriod() {
        return false;
    }

    public boolean isSpecializationDegreeRegistrationEvent() {
        return false;
    }

    public boolean isFctScholarshipPhdGratuityContribuitionEvent() {
        return false;
    }

    public boolean isTransferable() {
        return false;
    }

    public abstract Unit getOwnerUnit();

    public SortedSet<AccountingTransaction> getSortedTransactionsForPresentation() {
        final SortedSet<AccountingTransaction> result = new TreeSet<AccountingTransaction>(AccountingTransaction.COMPARATOR_BY_WHEN_REGISTERED);
        result.addAll(getAdjustedTransactions());
        return result;
    }

    public Person getPerson() {
        return (Person) getParty();
    }

    public DateTime getDueDateByPaymentCodes() {
        final YearMonthDay ymd = getPaymentCodesSet().stream().map(PaymentCode::getEndDate).max(YearMonthDay::compareTo).orElse(null);
        return ymd != null ? ymd.plusDays(1).toDateTimeAtMidnight() : getWhenOccured();
    }

    public List<String> getOperationsAfter(DateTime when) {
        List<String> result = new ArrayList<>();
        getNonAdjustingTransactions().forEach(e -> {
            if (e.getWhenProcessed().isAfter(when)) {
                result.add(getOperationLabel(e));
            }
        });

        getDebtExemptions().forEach(e -> {
            if (e.getWhenCreated().isAfter(when)) {
                result.add(getOperationsAfter(e));
            }
        });

        this.getDiscountsSet().forEach(d -> {
            if (d.getWhenCreated().isAfter(when)) {
                result.add(getOperationLabel(d));
            }
        });
        return result;
    }

    private String getOperationLabel(Discount d) {
        return BundleUtil.getString(Bundle.ACADEMIC, "label.accounting.operation.after.discount", d.getWhenCreated().toString());
    }

    private String getOperationsAfter(Exemption e) {
        return BundleUtil.getString(Bundle.ACADEMIC, "label.accounting.operation.after.exemption", e.getDescription().toString(),e
                .getWhenCreated()
                .toString());
    }

    private String getOperationLabel(AccountingTransaction e) {
        return BundleUtil.getString(Bundle.ACADEMIC, "label.accounting.operation.after.transaction", e
                .getWhenProcessed().toString());
    }

}
