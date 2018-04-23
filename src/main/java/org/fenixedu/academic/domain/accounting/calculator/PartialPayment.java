package org.fenixedu.academic.domain.accounting.calculator;

import java.math.BigDecimal;

import org.fenixedu.academic.domain.accounting.calculator.PartialPayment.View.Detailed;
import org.fenixedu.academic.domain.accounting.calculator.PartialPayment.View.Simple;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */

@JsonPropertyOrder({"amount"})
class PartialPayment<T extends DebtEntry> {

    interface View {
        interface Simple { }
        interface Detailed extends Simple { }
    }

    @JsonView(Detailed.class)
    private final CreditEntry creditEntry;

    @JsonView(CreditEntry.View.Detailed.class)
    @JsonProperty("target")
    private final T debtEntry;

    @JsonView(Simple.class)
    private final BigDecimal amount;

    public PartialPayment(CreditEntry creditEntry, T entry, BigDecimal amount) {
        this.creditEntry = creditEntry;
        this.debtEntry = entry;
        this.amount = amount;
    }

    public CreditEntry getCreditEntry() {
        return creditEntry;
    }

    public T getDebtEntry() {
        return debtEntry;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "PartialPayment{" + "amount=" + amount + '}';
    }


}
