package net.sourceforge.fenixedu.domain.accounting;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Contributor;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

import org.joda.time.DateTime;

public class Receipt extends Receipt_Base {

    public static Comparator<Receipt> COMPARATOR_BY_YEAR_AND_NUMBER = new Comparator<Receipt>() {
        public int compare(Receipt receipt, Receipt otherReceipt) {
            Integer yearComparationResult = receipt.getYear().compareTo(otherReceipt.getYear());
            if (yearComparationResult == 0) {
                return receipt.getNumber().compareTo(otherReceipt.getNumber());
            }
            return yearComparationResult;
        }
    };

    private Receipt() {
        super();
        super.setNumber(generateReceiptNumber());
        super.setRootDomainObject(RootDomainObject.getInstance());
        super.setWhenCreated(new DateTime());
    }

    public Receipt(Person person, Contributor contributor, List<Entry> entries) {
        this();
        init(person, contributor, entries);
    }

    private void init(Person person, Contributor contributor, List<Entry> entries) {
        checkParameters(person, contributor, entries);
        super.setPerson(person);
        super.setYear(new DateTime().getYear());
        super.setContributor(contributor);

        for (final Entry entry : entries) {
            entry.setReceipt(this);
        }
    }

    private void checkParameters(Party party, Contributor contributor, List<Entry> entries) {
        if (party == null) {
            throw new DomainException("error.accouting.receipt.party.cannot.be.null");
        }
        if (contributor == null) {
            throw new DomainException("error.accounting.receipt.contributor.cannot.be.null");
        }
        if (entries == null) {
            throw new DomainException("error.accounting.receipt.entries.cannot.be.null");
        }
        if (entries.isEmpty()) {
            throw new DomainException("error.accounting.receipt.entries.cannot.be.empty");
        }

    }

    @Override
    public void addEntries(Entry entries) {
        throw new DomainException("error.accounting.receipt.cannot.add.new.entries");
    }

    @Override
    public List<Entry> getEntries() {
        return Collections.unmodifiableList(super.getEntries());
    }

    @Override
    public Iterator<Entry> getEntriesIterator() {
        return getEntriesSet().iterator();
    }

    @Override
    public Set<Entry> getEntriesSet() {
        return Collections.unmodifiableSet(super.getEntriesSet());
    }

    @Override
    public void removeEntries(Entry entries) {
        throw new DomainException("error.accounting.receipt.cannot.remove.entries");
    }

    @Override
    public void setNumber(Integer number) {
        throw new DomainException("error.accounting.receipt.cannot.modify.number");
    }

    @Override
    public void removeReceiptsVersions(ReceiptPrintVersion receiptsVersions) {
        throw new DomainException("error.accounting.receipt.cannot.remove.receiptVersions");
    }

    @Override
    public void setPerson(Person person) {
        throw new DomainException("error.accounting.receipt.cannot.modify.person");
    }

    @Override
    public void setYear(Integer year) {
        throw new DomainException("error.accounting.receipt.cannot.modify.year");
    }

    @Override
    public void setWhenCreated(DateTime whenCreated) {
        throw new DomainException("error.accounting.receipt.cannot.modify.creation.date");
    }

    private Integer generateReceiptNumber() {
        final List<Receipt> receipts = RootDomainObject.getInstance().getReceipts();
        return receipts.isEmpty() ? 1 : Collections.max(receipts, Receipt.COMPARATOR_BY_YEAR_AND_NUMBER)
                .getNumber() + 1;
    }

    public ReceiptPrintVersion createReceiptVersion(Employee employee) {
        return new ReceiptPrintVersion(this, employee);
    }

    public ReceiptPrintVersion getMostRecentReceiptPrintVersion() {

        ReceiptPrintVersion result = null;
        for (final ReceiptPrintVersion receiptVersion : getReceiptsVersionsSet()) {
            if (result == null || receiptVersion.getWhenCreated().isAfter(result.getWhenCreated())) {
                result = receiptVersion;
            }
        }
        return result;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal result = new BigDecimal("0");
        for (final Entry entry : getEntriesSet()) {
            result = result.add(entry.getAmountWithAdjustment());
        }
        return result;
    }

}
