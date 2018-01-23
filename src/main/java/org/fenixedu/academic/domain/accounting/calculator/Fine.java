package org.fenixedu.academic.domain.accounting.calculator;

import java.math.BigDecimal;

import org.fenixedu.academic.domain.accounting.calculator.Interest.View.Detailed;
import org.fenixedu.academic.domain.accounting.calculator.Interest.View.Simple;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
@JsonPropertyOrder({"type", "date", "amount","origin"})
class Fine extends DebtEntry {
    interface View {
        interface Simple extends DebtEntry.View.Simple {}
        interface Detailed extends Simple, DebtEntry.View.Detailed {}
    }

    @JsonView(Simple.class)
    private final LocalDate date;

    @JsonView(Detailed.class)
    private final CreditEntry origin;

    public Fine(LocalDate date, BigDecimal amount, CreditEntry origin) {
        super("", date.toDateTimeAtStartOfDay(), date, "", amount);
        this.date = date;
        this.origin = origin;
    }

    public LocalDate getDate() {
        return date;
    }

    public CreditEntry getOrigin() {
        return origin;
    }

    @Override
    public String toString() {
        return "Interest{" + "date=" + date + "} " + super.toString();
    }

    @Override
    boolean isToDeposit(CreditEntry creditEntry) {
        return creditEntry.isForFine();
    }
}
