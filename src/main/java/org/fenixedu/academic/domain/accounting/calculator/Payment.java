package org.fenixedu.academic.domain.accounting.calculator;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public class Payment extends CreditEntry {

    public Payment(String id, DateTime created, LocalDate date, String description, BigDecimal amount) {
        super(id, created, date, description, amount);
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

    @Override
    public boolean isToApplyFine() {
        return true;
    }

    @Override
    public boolean isForFine() {
        return true;
    }
}
