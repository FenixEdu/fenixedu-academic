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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemption;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public abstract class Event extends Event_Base {

    public static final Comparator<Event> COMPARATOR_BY_DATE = new Comparator<Event>() {
        @Override
        public int compare(final Event e1, final Event e2) {
            final int i = e1.getWhenOccured().compareTo(e2.getWhenOccured());
            return i == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(e1, e2) : i;
        }
    };

    protected Event() {
        super();

        super.setRootDomainObject(Bennu.getInstance());
        super.setWhenOccured(new DateTime());
        super.setCreatedBy(AccessControl.getPerson() != null ? AccessControl.getPerson().getIstUsername() : null);

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

    @Override
    public EventState getEventState() {
        throw new DomainException(
                "error.net.sourceforge.fenixedu.domain.accounting.Event.dot.not.call.this.method.directly.use.isInState.instead");
    }

    protected EventState getCurrentEventState() {
        return super.getEventState();
    }

    public boolean isInState(final EventState eventState) {
        return super.getEventState() == eventState;
    }

    public final Set<Entry> process(final User responsibleUser, final Collection<EntryDTO> entryDTOs,
            final AccountingTransactionDetailDTO transactionDetail) {
        if (entryDTOs.isEmpty()) {
            throw new DomainException("error.accounting.Event.process.requires.entries.to.be.processed");
        }

        checkConditionsToProcessEvent(transactionDetail);

        final Set<Entry> result = internalProcess(responsibleUser, entryDTOs, transactionDetail);

        recalculateState(transactionDetail.getWhenRegistered());

        return result;

    }

    public final Set<Entry> process(final User responsibleUser, final AccountingEventPaymentCode paymentCode,
            final Money amountToPay, final SibsTransactionDetailDTO transactionDetailDTO) {

        checkConditionsToProcessEvent(transactionDetailDTO);

        final Set<Entry> result = internalProcess(responsibleUser, paymentCode, amountToPay, transactionDetailDTO);

        recalculateState(transactionDetailDTO.getWhenRegistered());

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

    protected Set<Entry> internalProcess(User responsibleUser, AccountingEventPaymentCode paymentCode, Money amountToPay,
            SibsTransactionDetailDTO transactionDetail) {

        throw new UnsupportedOperationException("error.net.sourceforge.fenixedu.domain.accounting.Event.operation.not.supported");
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

    @Override
    public void setEventStateDate(DateTime eventStateDate) {
        check(this, AcademicPredicates.MANAGE_PAYMENTS);
        super.setEventStateDate(eventStateDate);
    }

    protected boolean canCloseEvent(DateTime whenRegistered) {
        return calculateAmountToPay(whenRegistered).lessOrEqualThan(Money.ZERO);
    }

    public Set<Entry> getPositiveEntries() {
        final Set<Entry> result = new HashSet<Entry>();
        for (final AccountingTransaction transaction : getNonAdjustingTransactions()) {
            if (transaction.getToAccountEntry().getAmountWithAdjustment().isPositive()) {
                result.add(transaction.getToAccountEntry());
            }
        }

        return result;
    }

    public Set<Entry> getEntriesWithoutReceipt() {
        final Set<Entry> result = new HashSet<Entry>();
        for (final AccountingTransaction transaction : getNonAdjustingTransactions()) {
            if (!transaction.isSourceAccountFromParty(getPerson())) {
                continue;
            }

            final Entry entry = transaction.getToAccountEntry();
            if (!entry.isAssociatedToAnyActiveReceipt() && entry.isAmountWithAdjustmentPositive()) {
                result.add(entry);
            }
        }

        return result;
    }

    public List<AccountingTransaction> getNonAdjustingTransactions() {
        final List<AccountingTransaction> result = new ArrayList<AccountingTransaction>();

        for (final AccountingTransaction transaction : super.getAccountingTransactionsSet()) {
            if (!transaction.isAdjustingTransaction() && transaction.getAmountWithAdjustment().isPositive()) {
                result.add(transaction);
            }
        }
        return result;
    }

    public List<AccountingTransaction> getAllAdjustedAccountingTransactions() {
        final List<AccountingTransaction> result = new ArrayList<AccountingTransaction>();

        for (final AccountingTransaction transaction : super.getAccountingTransactionsSet()) {
            if (transaction.isAdjustingTransaction()) {
                result.add(transaction);
            }
        }

        return result;
    }

    public List<AccountingTransaction> getAdjustedTransactions() {
        final List<AccountingTransaction> result = new ArrayList<AccountingTransaction>();

        for (final AccountingTransaction transaction : super.getAccountingTransactionsSet()) {
            if (!transaction.isAdjustingTransaction()) {
                result.add(transaction);
            }
        }

        return result;
    }

    public List<AccountingTransaction> getSortedNonAdjustingTransactions() {
        final List<AccountingTransaction> result = getNonAdjustingTransactions();
        Collections.sort(result, AccountingTransaction.COMPARATOR_BY_WHEN_REGISTERED);

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
            throw new DomainException(
                    "error.accounting.Event.cannot.calculate.max.deductable.amount.for.legal.taxes.on.invalid.events");
        }

        if (isOpen() || !hasEventCloseDate()) {
            return calculatePayedAmountByPersonFor(civilYear);
        }

        final Money maxAmountForCivilYear =
                calculateTotalAmountToPay(getEventCloseDate()).subtract(getPayedAmountUntil(civilYear - 1)).subtract(
                        calculatePayedAmountByOtherPartiesFor(civilYear));

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
        for (final AccountingTransaction transaction : getNonAdjustingTransactions()) {
            if (transaction.isSourceAccountFromParty(getPerson()) && transaction.isPayed(civilYear)) {
                return true;
            }
        }

        return false;
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
        for (final AccountingTransaction accountingTransaction : getNonAdjustingTransactions()) {
            if (accountingTransaction.isPayed(civilYear)) {
                return true;
            }
        }

        return false;
    }

    public final void recalculateState(final DateTime whenRegistered) {
        if (isCancelled()) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.accounting.Event.cannot.recalculate.state.on.cancelled.events");
        }

        internalRecalculateState(whenRegistered);
    }

    protected void internalRecalculateState(final DateTime whenRegistered) {
        final DateTime previousStateDate = getEventStateDate();
        final EventState previousState = super.getEventState();

        if (isOpen()) {
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

        if (totalAmountToPay == null) {
            return Money.ZERO;
        }

        final Money remainingAmount = totalAmountToPay.subtract(getPayedAmount(whenRegistered));

        return remainingAmount.isPositive() ? remainingAmount : Money.ZERO;

    }

    private Money calculateTotalAmountToPay(DateTime whenRegistered) {
        return getPostingRule().calculateTotalAmountToPay(this, whenRegistered);
    }

    public Money getAmountToPay() {
        return calculateAmountToPay(new DateTime());
    }

    public Money getTotalAmountToPay(final DateTime whenRegistered) {
        final Money totalAmountToPay = calculateTotalAmountToPay(whenRegistered);

        return totalAmountToPay;
    }

    public Money getTotalAmountToPay() {
        return getTotalAmountToPay(new DateTime());
    }

    public Money getOriginalAmountToPay() {
        return getTotalAmountToPay(getWhenOccured().plusSeconds(1));
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

    public void cancel(final Person responsible, final String cancelJustification) {
        if (isCancelled()) {
            return;
        }

        checkRulesToCancel(responsible);

        changeState(EventState.CANCELLED, new DateTime());
        super.setResponsibleForCancel(responsible);
        super.setCancelJustification(cancelJustification);
        closeNonProcessedCodes();
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

    private void checkRulesToCancel(final Person responsible) {
        if (!responsible.hasRole(RoleType.MANAGER) && !isOpen()) {
            throw new DomainException("error.accounting.Event.only.open.events.can.be.cancelled");
        }

        if (getPayedAmount().isPositive()) {
            throw new DomainException("error.accounting.Event.cannot.cancel.events.with.payed.amount.greater.than.zero");
        }

    }

    protected Set<Entry> internalProcess(User responsibleUser, Collection<EntryDTO> entryDTOs,
            AccountingTransactionDetailDTO transactionDetail) {
        return getPostingRule().process(responsibleUser, entryDTOs, this, getFromAccount(), getToAccount(), transactionDetail);
    }

    public boolean hasInstallments() {
        return false;
    }

    public List<AccountingEventPaymentCode> calculatePaymentCodes() {
        return getAllPaymentCodes().isEmpty() ? createPaymentCodes() : updatePaymentCodes();
    }

    protected List<AccountingEventPaymentCode> updatePaymentCodes() {
        return Collections.EMPTY_LIST;
    }

    protected List<AccountingEventPaymentCode> createPaymentCodes() {
        return Collections.EMPTY_LIST;
    }

    public List<AccountingEventPaymentCode> getNonProcessedPaymentCodes() {
        final List<AccountingEventPaymentCode> result = new ArrayList<AccountingEventPaymentCode>();
        for (final AccountingEventPaymentCode paymentCode : super.getPaymentCodesSet()) {
            if (paymentCode.isNew()) {
                result.add(paymentCode);
            }
        }
        return result;
    }

    public boolean hasNonProcessedPaymentCodes() {
        for (final AccountingEventPaymentCode paymentCode : super.getPaymentCodesSet()) {
            if (paymentCode.isNew()) {
                return true;
            }
        }
        return false;
    }

    public List<AccountingEventPaymentCode> getCancelledPaymentCodes() {
        final List<AccountingEventPaymentCode> result = new ArrayList<AccountingEventPaymentCode>();
        for (final AccountingEventPaymentCode paymentCode : super.getPaymentCodesSet()) {
            if (paymentCode.isCancelled()) {
                result.add(paymentCode);
            }
        }

        return result;
    }

    public Set<AccountingEventPaymentCode> getAllPaymentCodes() {
        return Collections.unmodifiableSet(super.getPaymentCodesSet());
    }

    @Override
    public void addPaymentCodes(AccountingEventPaymentCode paymentCode) {
        throw new DomainException("error.net.sourceforge.fenixedu.domain.accounting.Event.cannot.add.paymentCode");
    }

    @Override
    @Deprecated
    public Set<AccountingEventPaymentCode> getPaymentCodesSet() {
        //  Not really deprecated, but be advised, this method should be returning the code below, but framework says no.
        //  Can`t throw DomainException because it is called from the Event_Base.class. Should not be called from anywhere else.
        //        throw new DomainException(
        //                "error.net.sourceforge.fenixedu.domain.accounting.Event.paymentCodes.cannot.be.accessed.directly");
        return super.getPaymentCodesSet();
    }

    @Override
    public void removePaymentCodes(AccountingEventPaymentCode paymentCode) {
        throw new DomainException("error.net.sourceforge.fenixedu.domain.accounting.Event.cannot.remove.paymentCode");
    }

    public static List<Event> readNotCancelled() {
        final List<Event> result = new ArrayList<Event>();

        for (final Event event : Bennu.getInstance().getAccountingEventsSet()) {
            if (!event.isCancelled()) {
                result.add(event);
            }
        }

        return result;

    }

    public PaymentCodeState getPaymentCodeStateFor(final PaymentMode paymentMode) {
        return (paymentMode == PaymentMode.ATM) ? PaymentCodeState.PROCESSED : PaymentCodeState.CANCELLED;
    }

    public LabelFormatter getDescription() {
        final LabelFormatter result = new LabelFormatter();
        result.appendLabel(getEventType().getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES);
        return result;
    }

    protected YearMonthDay calculateNextEndDate(final YearMonthDay yearMonthDay) {
        final YearMonthDay nextMonth = yearMonthDay.plusMonths(1);
        return new YearMonthDay(nextMonth.getYear(), nextMonth.getMonthOfYear(), 1).minusDays(1);
    }

    public Money getReimbursableAmount() {
        if (!isClosed() || !hasEventCloseDate()) {
            return Money.ZERO;
        }

        final Money extraPayedAmount = getPayedAmount().subtract(calculateTotalAmountToPay(getEventCloseDate()));

        if (extraPayedAmount.isPositive()) {
            final Money amountPayedByPerson = calculatePayedAmountByPerson();
            return amountPayedByPerson.lessOrEqualThan(extraPayedAmount) ? amountPayedByPerson : extraPayedAmount;
        }

        return Money.ZERO;

    }

    protected boolean hasEventCloseDate() {
        return getEventCloseDate() != null;
    }

    protected DateTime getEventCloseDate() {
        for (final AccountingTransaction transaction : getSortedNonAdjustingTransactions()) {
            if (canCloseEvent(transaction.getWhenRegistered())) {
                return transaction.getWhenRegistered();
            }
        }

        return null;

    }

    private Money calculatePayedAmountByPerson() {
        Money result = Money.ZERO;

        for (final AccountingTransaction transaction : getNonAdjustingTransactions()) {
            if (transaction.isSourceAccountFromParty(getPerson())) {
                result = result.add(transaction.getToAccountEntry().getAmountWithAdjustment());
            }
        }

        return result;
    }

    public final void forceChangeState(EventState state, DateTime when) {
        check(this, AcademicPredicates.MANAGE_PAYMENTS);
        changeState(state, when);
    }

    protected void changeState(EventState state, DateTime when) {
        super.setEventState(state);
        super.setEventStateDate(when);
    }

    public boolean isOtherPartiesPaymentsSupported() {
        return false;
    }

    public final void addOtherPartyAmount(User responsibleUser, Party party, Money amount,
            AccountingTransactionDetailDTO transactionDetailDTO) {

        getPostingRule().addOtherPartyAmount(responsibleUser, this, party.getAccountBy(AccountType.EXTERNAL), getToAccount(),
                amount, transactionDetailDTO);

        recalculateState(transactionDetailDTO.getWhenRegistered());
    }

    public final AccountingTransaction depositAmount(final User responsibleUser, final Money amount,
            final AccountingTransactionDetailDTO transactionDetailDTO) {

        final AccountingTransaction result =
                getPostingRule().depositAmount(responsibleUser, this, getParty().getAccountBy(AccountType.EXTERNAL),
                        getToAccount(), amount, transactionDetailDTO);

        recalculateState(transactionDetailDTO.getWhenRegistered());

        return result;
    }

    public final AccountingTransaction depositAmount(final User responsibleUser, final Money amount, final EntryType entryType,
            final AccountingTransactionDetailDTO transactionDetailDTO) {

        final AccountingTransaction result =
                getPostingRule().depositAmount(responsibleUser, this, getParty().getAccountBy(AccountType.EXTERNAL),
                        getToAccount(), amount, entryType, transactionDetailDTO);

        recalculateState(transactionDetailDTO.getWhenRegistered());

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
        final Set<Entry> result = new HashSet<Entry>();
        for (final AccountingTransaction transaction : getNonAdjustingTransactions()) {
            if (!transaction.isSourceAccountFromParty(getPerson())) {
                result.add(transaction.getToAccountEntry());
            }
        }

        return result;
    }

    public void rollbackCompletly() {
        check(this, AcademicPredicates.MANAGE_PAYMENTS);
        while (!getNonAdjustingTransactions().isEmpty()) {
            getNonAdjustingTransactions().iterator().next().delete();
        }

        changeState(EventState.OPEN, new DateTime());

        for (final PaymentCode paymentCode : getExistingPaymentCodes()) {
            paymentCode.setState(PaymentCodeState.NEW);
        }
    }

    public Set<AccountingEventPaymentCode> getExistingPaymentCodes() {
        check(this, AcademicPredicates.MANAGE_PAYMENTS);
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

        while (!getExemptions().isEmpty()) {
            getExemptions().iterator().next().delete(false);
        }

        super.setParty(null);
        super.setResponsibleForCancel(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    protected void checkRulesToDelete() {
        if (isClosed() || !getNonAdjustingTransactions().isEmpty()) {
            throw new DomainException(
                    "error.accounting.Event.cannot.delete.because.event.is.already.closed.or.has.transactions.associated");

        }
    }

    public static List<Event> readBy(final EventType eventType) {

        final List<Event> result = new ArrayList<Event>();
        for (final Event event : Bennu.getInstance().getAccountingEventsSet()) {
            if (event.getEventType() == eventType) {
                result.add(event);
            }
        }

        return result;

    }

    public static List<Event> readWithPaymentsByPersonForCivilYear(int civilYear) {
        final List<Event> result = new ArrayList<Event>();
        for (final Event event : readNotCancelled()) {
            if (event.hasPaymentsByPersonForCivilYear(civilYear)) {
                result.add(event);
            }
        }

        return result;

    }

    @Override
    public void addExemptions(Exemption exemption) {
        throw new DomainException("error.net.sourceforge.fenixedu.domain.accounting.Event.cannot.add.exemption");
    }

    @Override
    public Set<Exemption> getExemptionsSet() {
        return Collections.unmodifiableSet(super.getExemptionsSet());
    }

    @Override
    public void removeExemptions(Exemption exemption) {
        throw new DomainException("error.net.sourceforge.fenixedu.domain.accounting.Event.cannot.remove.exemption");
    }

    public boolean isExemptionAppliable() {
        return false;
    }

    public List<PenaltyExemption> getPenaltyExemptions() {
        final List<PenaltyExemption> result = new ArrayList<PenaltyExemption>();

        for (final Exemption exemption : getExemptionsSet()) {
            if (exemption instanceof PenaltyExemption) {
                result.add((PenaltyExemption) exemption);
            }
        }

        return result;
    }

    public boolean hasAnyPenaltyExemptionsFor(Class type) {
        for (final Exemption exemption : getExemptionsSet()) {
            if (exemption.getClass().equals(type)) {
                return true;
            }
        }

        return false;

    }

    public List<PenaltyExemption> getPenaltyExemptionsFor(Class type) {
        final List<PenaltyExemption> result = new ArrayList<PenaltyExemption>();

        for (final Exemption exemption : getExemptionsSet()) {
            if (exemption.getClass().equals(type)) {
                result.add((PenaltyExemption) exemption);
            }
        }

        return result;

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

            final AccountingTransactionDetailDTO transactionDetail =
                    createAccountingTransactionDetailForTransfer(entryToTransfer.getAccountingTransaction());

            targetEvent.depositAmount(responsible.getUser(), entryToTransfer.getAmountWithAdjustment(), transactionDetail);

            entryToTransfer.getAccountingTransaction().reimburseWithoutRules(responsible.getUser(), PaymentMode.CASH,
                    entryToTransfer.getAmountWithAdjustment());
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
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.accounting.Event.target.event.must.be.different.from.source");
        }
    }

    private AccountingTransactionDetailDTO createAccountingTransactionDetailForTransfer(final AccountingTransaction transaction) {
        final String comments =
                transaction.getEvent().getClass().getName() + ":" + transaction.getEvent().getExternalId() + ","
                        + transaction.getClass().getName() + ":" + transaction.getExternalId();

        return new AccountingTransactionDetailDTO(transaction.getTransactionDetail().getWhenRegistered(), PaymentMode.CASH,
                comments);

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

    public Set<EntryType> getPossibleEntryTypesForDeposit() {
        return Collections.EMPTY_SET;
    }

    public boolean isDepositSupported() {
        return !isCancelled() && !getPossibleEntryTypesForDeposit().isEmpty();
    }

    public void addDiscount(final Person responsible, final Money amount) {
        addDiscounts(new Discount(responsible, amount));
    }

    public Money getTotalDiscount() {
        Money result = Money.ZERO;
        for (final Discount discount : getDiscounts()) {
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

    public abstract Unit getOwnerUnit();

    public SortedSet<AccountingTransaction> getSortedTransactionsForPresentation() {
        final SortedSet<AccountingTransaction> result =
                new TreeSet<AccountingTransaction>(AccountingTransaction.COMPARATOR_BY_WHEN_REGISTERED);
        result.addAll(getAdjustedTransactions());
        return result;
    }

    public Person getPerson() {
        return (Person) getParty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.Discount> getDiscounts() {
        return getDiscountsSet();
    }

    @Deprecated
    public boolean hasAnyDiscounts() {
        return !getDiscountsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.Exemption> getExemptions() {
        return getExemptionsSet();
    }

    @Deprecated
    public boolean hasAnyExemptions() {
        return !getExemptionsSet().isEmpty();
    }

    @Deprecated
    public boolean hasResponsibleForCancel() {
        return getResponsibleForCancel() != null;
    }

    @Deprecated
    public boolean hasEventState() {
        return getEventState() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEventStateDate() {
        return getEventStateDate() != null;
    }

    @Deprecated
    public boolean hasParty() {
        return getParty() != null;
    }

    @Deprecated
    public boolean hasCreatedBy() {
        return getCreatedBy() != null;
    }

    @Deprecated
    public boolean hasWhenSentLetter() {
        return getWhenSentLetter() != null;
    }

    @Deprecated
    public boolean hasWhenOccured() {
        return getWhenOccured() != null;
    }

    @Deprecated
    public boolean hasEventType() {
        return getEventType() != null;
    }

    @Deprecated
    public boolean hasCancelJustification() {
        return getCancelJustification() != null;
    }

}
