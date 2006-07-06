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
    protected AccountingTransaction makeAccountingTransaction(User responsibleUser, Event event,
            Account from, Account to, EntryType entryType, BigDecimal amount, PaymentMode paymentMode,
            DateTime whenRegistered) {
        return new AccountingTransaction(responsibleUser, event, makeEntry(entryType, amount.negate(),
                from), makeEntry(entryType, amount, to), paymentMode, whenRegistered);
    }

    public boolean isClosed() {
        return getClosed().booleanValue();
    }

    public final Set<Entry> process(User responsibleUser, List<EntryDTO> entryDTOs,
            PaymentMode paymentMode) {
        return process(responsibleUser, entryDTOs, paymentMode, new DateTime());
    }

    public final Set<Entry> process(User responsibleUser, List<EntryDTO> entryDTOs,
            PaymentMode paymentMode, DateTime whenRegistered) {
        if (entryDTOs.isEmpty()) {
            throw new DomainException("error.accounting.event.process.requires.entries.to.be.processed");
        }
        if (!isClosed()) {
            return internalProcess(responsibleUser, entryDTOs, paymentMode, whenRegistered);
        } else {
            throw new DomainException("error.accounting.event.is.already.closed");
        }
    }

    public void closeEvent() {
        super.setClosed(Boolean.TRUE);
    }

    protected abstract Set<Entry> internalProcess(User responsibleUser, List<EntryDTO> entryDTOs,
            PaymentMode paymentMode, DateTime whenRegistered);

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

    protected boolean canCloseEvent(DateTime whenRegistered) {
        return calculateAmountToPay(whenRegistered).equals(calculatePayedAmount());
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

    protected BigDecimal calculatePayedAmount() {
        BigDecimal payedAmount = new BigDecimal("0");
        final Account account = getToAccount();
        for (final AccountingTransaction transaction : getAccountingTransactions()) {
            payedAmount = payedAmount.add(transaction.getAmountByAccount(account));
        }

        return payedAmount;
    }

    public abstract Account getToAccount();

    public abstract List<EntryDTO> calculateEntries();

    protected abstract BigDecimal calculateAmountToPay(DateTime whenRegistered);

    public abstract LabelFormatter getDescriptionForEntryType(EntryType entryType);

}
