package org.fenixedu.academic.domain.accounting.calculator;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.fenixedu.academic.domain.accounting.calculator.DebtEntry.View.Detailed;
import org.fenixedu.academic.domain.accounting.calculator.DebtEntry.View.Simple;

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
    @Type(value = Interest.class, name = "interest")
})
abstract class DebtEntry implements Cloneable {
    interface View {
        interface Simple {

        }
        interface Detailed {

        }
    }

    @JsonView(Simple.class)
    private final BigDecimal amount;

    @JsonView(Detailed.class)
    private final Set<PartialPayment> partialPayments = new HashSet<>();

    public DebtEntry(BigDecimal amount) {
        this.amount = amount;
    }

    public void addPartialPayment(CreditEntry creditEntry, BigDecimal amount) {
        PartialPayment<DebtEntry> partialPayment = new PartialPayment<>(creditEntry, this, amount);
        creditEntry.addPartialPayment(partialPayment);
        partialPayments.add(partialPayment);
    }

    public Set<PartialPayment> getPartialPayments() {
        return Collections.unmodifiableSet(partialPayments);
    }

    public BigDecimal getOriginalAmount() {
        return amount;
    }

    public BigDecimal getOpenAmount() {
        BigDecimal diff = getOriginalAmount().subtract(getPayedAmount());
        return diff.compareTo(BigDecimal.ZERO) > 0 ? diff : BigDecimal.ZERO;
    }
    
    public BigDecimal getPayedAmount() {
        return getAmount(partialPayment -> true);
    }

    private BigDecimal getAmount(Predicate<PartialPayment> filter) {
        return partialPayments.stream().filter(filter::test).map(PartialPayment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
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

    public boolean isOpen() {
        return getOpenAmount().compareTo(BigDecimal.ZERO) > 0;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DebtEntry)) {
            return false;
        }

        DebtEntry debtEntry = (DebtEntry) o;

        return (amount != null ? amount.equals(debtEntry.amount) : debtEntry.amount == null) && partialPayments.equals(debtEntry.partialPayments);
    }

    @Override
    public int hashCode() {
        int result = amount != null ? amount.hashCode() : 0;
        return result;
    }

    @Override
    public String toString() {
        return "amount=" + amount + ", partialPayments=" + partialPayments + ", originalAmount=" + getOriginalAmount() + ", openAmount=" + getOpenAmount() + ", payedAmount="
                   + getPayedAmount() + ", open=" + isOpen();
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
