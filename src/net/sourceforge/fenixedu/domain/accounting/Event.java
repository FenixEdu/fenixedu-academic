package net.sourceforge.fenixedu.domain.accounting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.accountingTransactions.InstallmentAccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public abstract class Event extends Event_Base {

    protected Event() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setOjbConcreteClass(getClass().getName());
	super.setWhenOccured(new DateTime());
	super.setEventState(EventState.OPEN);
    }

    protected void init(EventType eventType, Person person) {
	init(null, eventType, person);
    }

    protected void init(AdministrativeOffice administrativeOffice, EventType eventType, Person person) {
	checkParameters(eventType, person);
	super.setAdministrativeOffice(administrativeOffice);
	super.setEventType(eventType);
	super.setPerson(person);

    }

    private void checkParameters(EventType eventType, Person person) throws DomainException {
	if (eventType == null) {
	    throw new DomainException("error.accounting.Event.invalid.eventType");
	}
	if (person == null) {
	    throw new DomainException("error.accounting.person.cannot.be.null");
	}

    }

    public boolean isOpen() {
	return (getEventState() == EventState.OPEN);
    }

    public boolean isInDebt() {
	return isOpen();
    }

    public boolean isClosed() {
	return (getEventState() == EventState.CLOSED);
    }

    public boolean isPayed() {
	return isClosed();
    }

    public boolean isCancelled() {
	return (getEventState() == EventState.CANCELLED);
    }

    public final Set<Entry> process(final User responsibleUser, final List<EntryDTO> entryDTOs,
	    final AccountingTransactionDetailDTO transactionDetail) {
	if (entryDTOs.isEmpty()) {
	    throw new DomainException("error.accounting.Event.process.requires.entries.to.be.processed");
	}

	checkConditionsToProcessEvent(transactionDetail);

	final Set<Entry> result = internalProcess(responsibleUser, entryDTOs, transactionDetail);

	recalculateState(transactionDetail.getWhenRegistered());

	return result;

    }

    public final Set<Entry> process(final User responsibleUser,
	    final AccountingEventPaymentCode paymentCode, final Money amountToPay,
	    final SibsTransactionDetailDTO transactionDetailDTO) {

	checkConditionsToProcessEvent(transactionDetailDTO);

	final Set<Entry> result = internalProcess(responsibleUser, paymentCode, amountToPay,
		transactionDetailDTO);

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

    protected Set<Entry> internalProcess(User responsibleUser, AccountingEventPaymentCode paymentCode,
	    Money amountToPay, SibsTransactionDetailDTO transactionDetail) {

	throw new UnsupportedOperationException(
		"error.net.sourceforge.fenixedu.domain.accounting.Event.operation.not.supported");
    }

    protected void closeEvent() {
	super.setEventState(EventState.CLOSED);
    }

    @Override
    public void addAccountingTransactions(AccountingTransaction accountingTransactions) {
	throw new DomainException("error.accounting.Event.cannot.add.accountingTransactions");
    }

    @Override
    public List<AccountingTransaction> getAccountingTransactions() {
	return Collections.unmodifiableList(super.getAccountingTransactions());
    }

    @Override
    public Set<AccountingTransaction> getAccountingTransactionsSet() {
	return Collections.unmodifiableSet(super.getAccountingTransactionsSet());
    }

    @Override
    public Iterator<AccountingTransaction> getAccountingTransactionsIterator() {
	return getAccountingTransactionsSet().iterator();
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
    public void setPerson(Person person) {
	throw new DomainException("error.accounting.Event.cannot.modify.person");
    }

    @Override
    public void setEventState(EventState eventState) {
	throw new DomainException("error.accounting.Event.cannot.modify.eventState");
    }

    @Override
    public void setAdministrativeOffice(AdministrativeOffice administrativeOffice) {
	throw new DomainException("error.accounting.Event.cannot.modify.administrativeOffice");
    }

    @Override
    public void setEmployeeResponsibleForCancel(Employee employee) {
	throw new DomainException("error.accounting.Event.cannot.modify.employeeResponsibleForCancel");
    }

    @Override
    public void setWhenCancelled(DateTime whenCancelled) {
	throw new DomainException("error.accounting.Event.cannot.modify.whenCancelled");
    }

    protected boolean canCloseEvent(DateTime whenRegistered) {
	return calculateAmountToPay(whenRegistered).lessOrEqualThan(Money.ZERO);
    }

    public Set<Entry> getEntries() {
	final Set<Entry> result = new HashSet<Entry>();
	for (final AccountingTransaction transaction : getAccountingTransactions()) {
	    result.add(transaction.getToAccountEntry());
	}

	return result;
    }

    public Set<Entry> getEntriesWithoutReceipt() {
	final Set<Entry> result = new HashSet<Entry>();
	for (final AccountingTransaction transaction : getAccountingTransactions()) {
	    final Entry entry = transaction.getToAccountEntry();
	    if (!entry.hasReceipt()) {
		result.add(entry);
	    }
	}

	return result;
    }

    public Money calculatePayedAmount() {
	if (isCancelled()) {
	    throw new DomainException(
		    "error.accounting.Event.cannot.calculatePayedAmount.on.invalid.events");
	}

	Money payedAmount = Money.ZERO;
	for (final AccountingTransaction transaction : getAccountingTransactions()) {
	    payedAmount = payedAmount.add(transaction.getToAccountEntry().getAmountWithAdjustment());
	}

	return payedAmount;
    }

    public Money getPayedAmount(final int civilYear) {
	Money result = Money.ZERO;

	for (final Entry entry : getPayedEntries(civilYear)) {
	    result = result.add(entry.getAmountWithAdjustment());
	}

	return result;
    }

    private Set<Entry> getPayedEntries(final int civilYear) {
	final Set<Entry> result = new HashSet<Entry>();

	for (final AccountingTransaction accountingTransaction : getAccountingTransactionsSet()) {
	    if (accountingTransaction.isPayed(civilYear)) {
		result.add(accountingTransaction.getToAccountEntry());
	    }
	}

	return result;
    }

    public void recalculateState(final DateTime whenRegistered) {
	if (canCloseEvent(whenRegistered)) {
	    closeNonProcessedCodes();
	    super.setEventState(EventState.CLOSED);
	}
    }

    private void closeNonProcessedCodes() {
	for (final AccountingEventPaymentCode paymentCode : getNonProcessedPaymentCodes()) {
	    paymentCode.setState(PaymentCodeState.CANCELLED);
	}
    }

    public Money calculateAmountToPay(DateTime whenRegistered) {
	final Money totalAmountToPay = getPostingRule(whenRegistered).calculateTotalAmountToPay(this,
		whenRegistered);
	return totalAmountToPay.isPositive() ? totalAmountToPay.subtract(calculatePayedAmount())
		: Money.ZERO;

    }

    public Money getAmountToPay() {
	return calculateAmountToPay(new DateTime());
    }

    public List<EntryDTO> calculateEntries() {
	return calculateEntries(new DateTime());
    }

    public List<EntryDTO> calculateEntries(DateTime when) {
	return getPostingRule(when).calculateEntries(this, when);
    }

    public final boolean isPayableOnAdministrativeOffice(AdministrativeOffice administrativeOffice) {
	return (!hasAdministrativeOffice() || getAdministrativeOffice() == administrativeOffice);
    }

    public void cancel(final Employee responsibleEmployee) {
	cancel(responsibleEmployee, null);
    }

    public void cancel(final Employee responsibleEmployee, final String cancelJustification) {
	checkRulesToCancel();
	super.setWhenCancelled(new DateTime());
	super.setEmployeeResponsibleForCancel(responsibleEmployee);
	super.setEventState(EventState.CANCELLED);
	super.setCancelJustification(cancelJustification);
	closeNonProcessedCodes();
    }

    private void checkRulesToCancel() {
	if (!isOpen()) {
	    throw new DomainException("error.accounting.Event.only.open.events.can.be.cancelled");
	}

	if (calculatePayedAmount().isPositive()) {
	    throw new DomainException(
		    "error.accounting.Event.cannot.cancel.events.with.payed.amount.greater.than.zero");
	}

    }

    protected Set<Entry> internalProcess(User responsibleUser, List<EntryDTO> entryDTOs,
	    AccountingTransactionDetailDTO transactionDetail) {
	return getPostingRule(transactionDetail.getWhenRegistered()).process(responsibleUser, entryDTOs,
		this, getFromAccount(), getToAccount(), transactionDetail);
    }

    public boolean hasAccountingTransactionFor(final Installment installment) {
	return getAccountingTransactionFor(installment) != null;
    }

    public InstallmentAccountingTransaction getAccountingTransactionFor(final Installment installment) {
	for (final AccountingTransaction accountingTransaction : getAccountingTransactionsSet()) {
	    if (accountingTransaction instanceof InstallmentAccountingTransaction
		    && ((InstallmentAccountingTransaction) accountingTransaction).getInstallment() == installment) {
		return (InstallmentAccountingTransaction) accountingTransaction;
	    }
	}

	return null;
    }

    public boolean hasInstallments() {
	return false;
    }

    public List<AccountingEventPaymentCode> calculatePaymentCodes() {
	return !hasAnyPaymentCodes() ? createPaymentCodes() : updatePaymentCodes();
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

    @Override
    public void addPaymentCodes(AccountingEventPaymentCode paymentCode) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.Event.cannot.add.paymentCode");
    }

    @Override
    public List<AccountingEventPaymentCode> getPaymentCodes() {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.Event.paymentCodes.cannot.be.accessed.directly");
    }

    @Override
    public Set<AccountingEventPaymentCode> getPaymentCodesSet() {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.Event.paymentCodes.cannot.be.accessed.directly");
    }

    @Override
    public Iterator<AccountingEventPaymentCode> getPaymentCodesIterator() {
	return getPaymentCodesSet().iterator();
    }

    @Override
    public void removePaymentCodes(AccountingEventPaymentCode paymentCode) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.Event.cannot.remove.paymentCode");
    }

    private static List<Event> readBy(final ExecutionYear executionYear, EventState eventState) {
	final List<Event> result = new ArrayList<Event>();
	for (final Event event : RootDomainObject.getInstance().getAccountingEventsSet()) {
	    if (event.getEventState() == eventState
		    && executionYear.containsDate(event.getWhenOccured())) {
		result.add(event);
	    }
	}

	return result;
    }

    public static List<Event> readNotPayedBy(final ExecutionYear executionYear) {
	return readBy(executionYear, EventState.OPEN);
    }

    public PaymentCodeState getPaymentCodeStateFor(final PaymentMode paymentMode) {
	return (paymentMode == PaymentMode.ATM) ? PaymentCodeState.PROCESSED
		: PaymentCodeState.CANCELLED;
    }

    public LabelFormatter getDescription() {
	final LabelFormatter result = new LabelFormatter();
	result.appendLabel(getEventType().getQualifiedName(), "enum");
	return result;
    }
    
    protected YearMonthDay calculateNextEndDate(final YearMonthDay yearMonthDay) {
	final YearMonthDay nextMonth = yearMonthDay.plusMonths(1);
	return new YearMonthDay(nextMonth.getYear(), nextMonth.getMonthOfYear(), 1).minusDays(1);
    }

    protected abstract Account getFromAccount();

    public abstract Account getToAccount();

    public abstract LabelFormatter getDescriptionForEntryType(EntryType entryType);

    protected abstract PostingRule getPostingRule(DateTime whenRegistered);

}