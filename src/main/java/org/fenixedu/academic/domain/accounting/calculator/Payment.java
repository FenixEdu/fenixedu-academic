package org.fenixedu.academic.domain.accounting.calculator;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public class Payment extends CreditEntry {

    public Payment(LocalDate date, BigDecimal amount) {
        super(date, amount);
    }

    public Payment(DateTime created, LocalDate date, BigDecimal amount) {
        super(created, date, amount);
    }

    @Override
    public boolean isForDebt() {
        return true;
    }

    @Override
    public boolean isForInterest() {
        return true;
    }

    @Override
    public boolean isToApplyInterest() {
        return true;
    }

}
