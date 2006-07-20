package net.sourceforge.fenixedu.domain.accounting;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
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

    protected void init(AdministrativeOffice administrativeOffice, EventType eventType, Person person) {
        checkParameters(administrativeOffice, eventType, person);
        super.setAdministrativeOffice(administrativeOffice);
        super.setEventType(eventType);
        super.setPerson(person);

    }

    private void checkParameters(AdministrativeOffice administrativeOffice, EventType eventType,
            Person person) throws DomainException {
        if (administrativeOffice == null) {
            throw new DomainException("error.accounting.Event.administrativeOffice.cannot.be.null");
        }
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

    protected boolean canCloseEvent(DateTime whenRegistered) {
        return calculateAmountToPay(whenRegistered).equals(calculatePayedAmount());
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

        BigDecimal payedAmount = new BigDecimal("0");
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

    private BigDecimal calculateAmountToPay(DateTime whenRegistered) {
        return getPostingRule(whenRegistered).calculateTotalAmountToPay(this, whenRegistered);

    }

    public List<EntryDTO> calculateEntries() {
        return calculateEntries(new DateTime());
    }

    public List<EntryDTO> calculateEntries(DateTime when) {
        return getPostingRule(when).calculateEntries(this, when);
    }

    public final boolean isPayableOnAdministrativeOffice(AdministrativeOffice administrativeOffice) {
        return (getAdministrativeOffice() == administrativeOffice);
    }

    public abstract Account getToAccount();

    public abstract LabelFormatter getDescriptionForEntryType(EntryType entryType);

    protected abstract PostingRule getPostingRule(DateTime whenRegistered);

    protected abstract Set<Entry> internalProcess(User responsibleUser, List<EntryDTO> entryDTOs,
            PaymentMode paymentMode, DateTime whenRegistered);

}
