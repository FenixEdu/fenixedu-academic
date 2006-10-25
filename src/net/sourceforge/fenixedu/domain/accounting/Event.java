package net.sourceforge.fenixedu.domain.accounting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;

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

    public boolean isClosed() {
	return (getEventState() == EventState.CLOSED);
    }

    public boolean isCancelled() {
	return (getEventState() == EventState.CANCELLED);
    }

    public final Set<Entry> process(User responsibleUser, List<EntryDTO> entryDTOs,
	    PaymentMode paymentMode) {
	return process(responsibleUser, entryDTOs, paymentMode, new DateTime());
    }

    public final Set<Entry> process(User responsibleUser, List<EntryDTO> entryDTOs,
	    PaymentMode paymentMode, DateTime whenRegistered) {
	if (entryDTOs.isEmpty()) {
	    throw new DomainException("error.accounting.Event.process.requires.entries.to.be.processed");
	}
	if (!isClosed()) {
	    final Set<Entry> result = internalProcess(responsibleUser, entryDTOs, paymentMode,
		    whenRegistered);

	    if (canCloseEvent(whenRegistered)) {
		closeEvent();
	    }

	    return result;
	} else {
	    throw new DomainException("error.accounting.Event.is.already.closed");
	}
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
	return calculateAmountToPay(whenRegistered).compareTo(BigDecimal.ZERO) == 0;
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

    protected BigDecimal calculatePayedAmount() {
	if (isCancelled()) {
	    throw new DomainException(
		    "error.accounting.Event.cannot.calculatePayedAmount.on.invalid.events");
	}

	BigDecimal payedAmount = BigDecimal.ZERO;
	for (final AccountingTransaction transaction : getAccountingTransactions()) {
	    payedAmount = payedAmount.add(transaction.getToAccountEntry().getAmountWithAdjustment());
	}

	return payedAmount;
    }

    void recalculateState(DateTime whenRegistered) {
	if (canCloseEvent(whenRegistered)) {
	    super.setEventState(EventState.CLOSED);
	}
    }

    public BigDecimal calculateAmountToPay(DateTime whenRegistered) {
	final BigDecimal totalAmountToPay = getPostingRule(whenRegistered).calculateTotalAmountToPay(
		this, whenRegistered);
	return (totalAmountToPay.compareTo(BigDecimal.ZERO) > 0) ? totalAmountToPay
		.subtract(calculatePayedAmount()) : BigDecimal.ZERO;

    }

    public List<EntryDTO> calculateEntries() {
	return calculateEntries(new DateTime());
    }

    public List<EntryDTO> calculateEntries(DateTime when) {
	final List<EntryDTO> result = new ArrayList<EntryDTO>();
	for (final GenericPair<EntryType, BigDecimal> entry : getPostingRule(when).calculateEntries(
		this, when)) {
	    result.add(new EntryDTO(entry.getLeft(), this, entry.getRight(), calculatePayedAmount(),
		    calculateAmountToPay(when), getDescriptionForEntryType(entry.getLeft()),
		    calculateAmountToPay(when)));
	}

	return result;
    }

    public final boolean isPayableOnAdministrativeOffice(AdministrativeOffice administrativeOffice) {
	return (!hasAdministrativeOffice() || getAdministrativeOffice() == administrativeOffice);
    }

    public void cancel(final Employee responsibleEmployee) {
	checkRulesToCancel();
	super.setWhenCancelled(new DateTime());
	super.setEmployeeResponsibleForCancel(responsibleEmployee);
	super.setEventState(EventState.CANCELLED);
    }

    private void checkRulesToCancel() {
	if (!isOpen()) {
	    throw new DomainException("error.accounting.Event.only.open.events.can.be.cancelled");
	}

	if (calculatePayedAmount().compareTo(BigDecimal.ZERO) != 0) {
	    throw new DomainException(
		    "error.accounting.Event.cannot.cancel.events.with.payed.amount.greater.or.equal.than.zero");
	}

    }

    protected Set<Entry> internalProcess(User responsibleUser, List<EntryDTO> entryDTOs,
	    PaymentMode paymentMode, DateTime whenRegistered) {
	return getPostingRule(whenRegistered).process(responsibleUser, entryDTOs, paymentMode,
		whenRegistered, this, getFromAccount(), getToAccount());
    }

    protected abstract Account getFromAccount();

    public abstract Account getToAccount();

    public abstract LabelFormatter getDescriptionForEntryType(EntryType entryType);

    protected abstract PostingRule getPostingRule(DateTime whenRegistered);

}
