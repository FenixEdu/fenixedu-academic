package org.fenixedu.academic.domain.accounting.calculator;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.accounting.calculator.CreditEntry.View.Detailed;
import org.fenixedu.academic.domain.accounting.calculator.CreditEntry.View.Simple;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */

@JsonPropertyOrder({ "type", "created", "date", "amount" })
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
    @Type(value = Payment.class, name = "payment"),
    @Type(value = DebtExemption.class, name = "debtExemption"),
    @Type(value = InterestExemption.class, name = "interestExemption"),
    @Type(value = FineExemption.class, name = "fineExemption")
})
abstract class CreditEntry implements Cloneable {

    interface View {
        interface Simple {
        }

        interface Detailed extends Simple {
        }
    }

    @JsonView(Simple.class)
    private DateTime created;
    @JsonView(Simple.class)
    private final LocalDate date;
    @JsonView(Simple.class)
    private final BigDecimal amount;

    @JsonView(Detailed.class)
    private final Set<PartialPayment> partialPayments = new HashSet<>();


    public CreditEntry(DateTime created, LocalDate date, BigDecimal amount) {
        this.created = created;
        this.date = date;
        this.amount = amount;
    }

    public abstract boolean isForDebt();

    public abstract boolean isForInterest();

    public abstract boolean isForFine();

    public abstract boolean isToApplyInterest();

    public abstract boolean isToApplyFine();

    public DateTime getCreated() {
        return created;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void addPartialPayment(PartialPayment partialPayment) {
        if (partialPayment.getAmount().compareTo(getUnusedAmount()) <= 0) {
            this.partialPayments.add(partialPayment);
        } else {
            throw new UnsupportedOperationException("amount greater than unused: " + partialPayment.getAmount() + ", " + getUnusedAmount());
        }
    }

    @JsonView(View.Detailed.class)
    public BigDecimal getUnusedAmount() {
        return getAmount().subtract(getUsedAmount());
    }

    @JsonView(View.Detailed.class)
    public BigDecimal getUsedAmount() {
        return partialPayments.stream().map(PartialPayment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getUsedAmount(Class<? extends DebtEntry> debtEntryClass) {
        return partialPayments.stream().filter(partialPayment -> debtEntryClass.isInstance(partialPayment.getDebtEntry())).map(PartialPayment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @JsonView(View.Detailed.class)
    public BigDecimal getUsedAmountInDebts() {
        return getUsedAmount(Debt.class);
    }

    @JsonView(View.Detailed.class)
    public BigDecimal getUsedAmountInInterests() {
        return getUsedAmount(Interest.class);
    }

    public Set<PartialPayment> getPartialPayments() {
        return Collections.unmodifiableSet(partialPayments);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "date=" + date + ", amount=" + amount + ", partialPayments=" + partialPayments + ", unusedAmount=" + getUnusedAmount() + ", usedAmount=" +
                   getUsedAmount()
                   + ", usedAmountInDebts=" + getUsedAmountInDebts() + ", usedAmountInInterests=" + getUsedAmountInInterests() + '}';
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }
}
