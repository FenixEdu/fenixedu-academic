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
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.LabelFormatter;

import org.joda.time.DateTime;

public abstract class Event extends Event_Base {

    protected Event() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(getClass().getName());
    }

    protected void init(EventType eventType, Person person) {
        checkParameters(eventType, person);
        super.setEventType(eventType);
        super.setWhenOccured(new DateTime());
        super.setClosed(Boolean.FALSE);
        super.setPerson(person);
    }

    private void checkParameters(EventType eventType, Person person) throws DomainException {
        if (eventType == null) {
            throw new DomainException("error.accounting.event.invalid.eventType");
        }
        if (person == null) {
            throw new DomainException("error.accounting.person.cannot.be.null");
        }

    }

    // TODO: to remove after create agreement and posting rules
    protected Entry makeEntry(EntryType entryType, BigDecimal amount, Account account) {
        return new Entry(entryType, amount, account);
    }

    // TODO: to remove after create agreement and posting rules
    protected void makeAccountingTransaction(User responsibleUser, Entry debit, Entry credit) {
        new AccountingTransaction(responsibleUser, this, debit, credit);
    }

    // TODO: to remove after create agreement and posting rules
    protected void makeAccountingTransaction(User responsibleUser, Event event, Account from,
            Account to, EntryType entryType, BigDecimal amount) {
        new AccountingTransaction(responsibleUser, event, makeEntry(entryType, amount.negate(), from),
                makeEntry(entryType, amount, to));
    }

    public boolean isClosed() {
        return getClosed().booleanValue();
    }

    public final void process(User responsibleUser, List<EntryDTO> entryDTOs) {
        if (entryDTOs.isEmpty()) {
            throw new DomainException("error.accounting.event.process.requires.entries.to.be.processed");
        }
        if (!isClosed()) {
            internalProcess(responsibleUser, entryDTOs);
        }
    }

    public void closeEvent() {
        super.setClosed(Boolean.TRUE);
    }

    protected abstract void internalProcess(User responsibleUser, List<EntryDTO> entryDTOs);

    @Override
    public void addAccountingTransactions(AccountingTransaction accountingTransactions) {
        throw new DomainException("error.accounting.event.cannot.add.accountingTransactions");
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
        throw new DomainException("error.accounting.event.cannot.remove.accountingTransactions");
    }

    @Override
    public void setEventType(EventType eventType) {
        throw new DomainException("error.accounting.event.cannot.modify.eventType");
    }

    @Override
    public void setClosed(Boolean closed) {
        throw new DomainException("error.accounting.event.cannot.modify.processed.value");
    }

    @Override
    public void setWhenOccured(DateTime whenOccured) {
        throw new DomainException("error.accounting.event.cannot.modify.occuredDateTime");
    }

    @Override
    public void setPerson(Person person) {
        throw new DomainException("error.accounting.event.cannot.modify.person");
    }

    protected boolean canCloseEvent() {
        return calculateAmountToPay().equals(calculatePayedAmount());
    }

    public Set<Entry> getEntries() {
        final Set<Entry> result = new HashSet<Entry>();
        for (final AccountingTransaction transaction : getAccountingTransactions()) {
            result.add(transaction.getEntryByAccount(getToAccount()));
        }

        return result;
    }

    public Set<Entry> getEntriesWithoutReceipt() {
        final Set<Entry> result = new HashSet<Entry>();
        for (final AccountingTransaction transaction : getAccountingTransactions()) {
            final Entry entry = transaction.getEntryByAccount(getToAccount());
            if (!entry.hasReceipt()) {
                result.add(entry);
            }
        }

        return result;
    }

    public abstract Account getToAccount();

    public abstract List<EntryDTO> calculateEntries();

    protected abstract BigDecimal calculateAmountToPay();

    protected abstract BigDecimal calculatePayedAmount();

    public abstract LabelFormatter getDescriptionForEntryType(EntryType entryType);

}
