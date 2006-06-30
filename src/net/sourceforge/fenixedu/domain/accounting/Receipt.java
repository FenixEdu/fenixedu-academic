package net.sourceforge.fenixedu.domain.accounting;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Contributor;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

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
        super.setRootDomainObject(RootDomainObject.getInstance());
    }

    Receipt(Party party, Contributor contributor, Integer year, Integer version, Entry... entries) {
        this();
        init(party, contributor, year, entries);
    }

    private void init(Party party, Contributor contributor, Integer year, Entry... entries) {
        checkParameters(party, contributor, year);
        super.setParty(party);
        super.setNumber(generateReceiptNumber());
        super.setContributor(contributor);
        super.setYear(year);

        for (final Entry entry : entries) {
            entry.setReceipt(this);
        }
    }

    private void checkParameters(Party party, Contributor contributor, Integer year) {
        if (party == null) {
            throw new DomainException("error.accouting.receipt.party.cannot.be.null");
        }
        if (contributor == null) {
            throw new DomainException("error.accounting.receipt.contributor.cannot.be.null");
        }
        if (year == null) {
            throw new DomainException("error.accounting.receipt.year.cannot.be.null");
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
    public void removeReceiptsVersions(ReceiptVersion receiptsVersions) {
        throw new DomainException("error.accounting.receipt.cannot.remove.receiptVersions");
    }

    @Override
    public void setParty(Party party) {
        throw new DomainException("error.accounting.receipt.cannot.modify.party");
    }

    @Override
    public void setYear(Integer year) {
        throw new DomainException("error.accounting.receipt.cannot.modify.year");
    }

    private Integer generateReceiptNumber() {
        return Collections.max(RootDomainObject.getInstance().getReceipts(),
                Receipt.COMPARATOR_BY_YEAR_AND_NUMBER).getNumber() + 1;

    }

    public ReceiptVersion createReceiptVersion(Employee employee) {
        return new ReceiptVersion(this, employee);
    }

    public ReceiptVersion getMostRecentReceiptVersion() {

        ReceiptVersion result = null;
        for (final ReceiptVersion receiptVersion : getReceiptsVersionsSet()) {
            if (result == null || receiptVersion.getWhenCreated().isAfter(result.getWhenCreated())) {
                result = receiptVersion;
            }
        }
        return result;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal result = new BigDecimal("0");
        for (final Entry entry : getEntriesSet()) {
            result = result.add(entry.getAmount());
        }
        return result;
    }

}
