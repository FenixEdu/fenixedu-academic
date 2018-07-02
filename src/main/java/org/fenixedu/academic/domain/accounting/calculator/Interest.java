package org.fenixedu.academic.domain.accounting.calculator;

import java.math.BigDecimal;

import org.fenixedu.academic.domain.accounting.InterestRate.InterestRateBean;
import org.fenixedu.academic.domain.accounting.calculator.Interest.View.Detailed;
import org.fenixedu.academic.domain.accounting.calculator.Interest.View.Simple;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
@JsonPropertyOrder({"type", "date", "amount","origin"})
class Interest extends DebtEntry {
    interface View {
        interface Simple extends DebtEntry.View.Simple {}
        interface Detailed extends Simple, DebtEntry.View.Detailed {}
    }

    @JsonView(Detailed.class)
    private final CreditEntry origin;


    @JsonView(Simple.class)
    private final InterestRateBean interestRateBean;

    public Interest(LocalDate date, BigDecimal amount, CreditEntry origin, InterestRateBean interestRateBean) {
        super("", date.toDateTimeAtStartOfDay(), date, "", amount);
        this.origin = origin;
        this.interestRateBean = interestRateBean;
    }

    public CreditEntry getOrigin() {
        return origin;
    }

    public InterestRateBean getInterestRateBean() {
        return interestRateBean;
    }

    @Override
    public String toString() {
        return "Interest{" + "date=" + getDate() + "} " + super.toString();
    }

    @Override
    boolean isToDeposit(CreditEntry creditEntry) {
        return creditEntry.isForInterest();
    }
}
