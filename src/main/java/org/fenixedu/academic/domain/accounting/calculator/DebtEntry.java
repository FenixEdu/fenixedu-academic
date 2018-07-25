package org.fenixedu.academic.domain.accounting.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.accounting.calculator.DebtEntry.View.Detailed;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
        @Type(value = Debt.class, name = "debt"),
        @Type(value = Interest.class, name = "interest"),
        @Type(value = Fine.class, name = "fine")
})
public abstract class DebtEntry extends AccountingEntry implements Cloneable {
    interface View {
        interface Simple extends AccountingEntry.View.Simple {

        }
        interface Detailed extends Simple {

        }
    }

    public DebtEntry(String id, DateTime created, LocalDate date, String description, BigDecimal amount) {
        super(id, created, date, description, amount);
    }

    @JsonView(Detailed.class)
    private final Set<PartialPayment> partialPayments = new HashSet<>();


    public void addPartialPayment(CreditEntry creditEntry, BigDecimal amount) {
        PartialPayment<DebtEntry> partialPayment = new PartialPayment<>(creditEntry, this, amount);
        creditEntry.addPartialPayment(partialPayment);
        partialPayments.add(partialPayment);
    }

    public List<PartialPayment> getPartialPayments() {
        return partialPayments.stream().sorted((a,b) -> Comparator.comparing(CreditEntry::getCreated).compare(a
                .getCreditEntry(), b.getCreditEntry())).collect(Collectors.toList());
    }

    public BigDecimal getOriginalAmount() {
        return getAmount();
    }

    public BigDecimal getOpenAmount() {
        BigDecimal diff = getOriginalAmount().subtract(getPaidAmount()).setScale(2, RoundingMode.HALF_EVEN);
        return diff.compareTo(BigDecimal.ZERO) > 0 ? diff : BigDecimal.ZERO;
    }

    public BigDecimal getPaidAmount() {
        return getAmount(partialPayment -> true);
    }

    private BigDecimal getAmount(Predicate<PartialPayment> filter) {
        return partialPayments.stream().filter(filter).map(PartialPayment::getAmount).map(amount -> amount.setScale(2, RoundingMode.HALF_EVEN)).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getPaymentsAmount() {
        return getAmount(partialPayment -> partialPayment.getCreditEntry() instanceof Payment);
    }

    public BigDecimal getDebtExemptionAmount() {
        return getAmount(partialPayment -> partialPayment.getCreditEntry() instanceof DebtExemption);
    }

    public BigDecimal getInterestExemptionAmount() {
        return getAmount(partialPayment -> partialPayment.getCreditEntry() instanceof InterestExemption);
    }

    public BigDecimal getFineExemptionAmount() {
        return getAmount(partialPayment -> partialPayment.getCreditEntry() instanceof FineExemption);
    }

    public boolean isOpen() {
        return getOpenAmount().setScale(2, RoundingMode.HALF_EVEN).compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public String toString() {
        return "amount=" + getAmount() + ", partialPayments=" + partialPayments + ", originalAmount=" + getOriginalAmount() + ", "
                + "openAmount=" + getOpenAmount() + ", paidAmount="
                + getPaidAmount() + ", open=" + isOpen();
    }

    abstract boolean isToDeposit(CreditEntry creditEntry);

    public void deposit(CreditEntry creditEntry) {
        if (isToDeposit(creditEntry)) {
            BigDecimal openAmount = getOpenAmount();
            BigDecimal unusedAmount = creditEntry.getUnusedAmount();
            if (openAmount.compareTo(BigDecimal.ZERO) > 0 && unusedAmount.compareTo(BigDecimal.ZERO) > 0) {
                addPartialPayment(creditEntry, unusedAmount.compareTo(openAmount) >= 0 ? openAmount : unusedAmount);
            }
        }
    }
}
