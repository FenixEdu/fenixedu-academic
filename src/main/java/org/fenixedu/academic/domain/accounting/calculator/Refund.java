package org.fenixedu.academic.domain.accounting.calculator;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Represents a refund of the paid amount.
 * The amount is not used to credit a debt entry, instead this {@link CreditEntry} will be used as a placeholder in
 * {@link DebtInterestCalculator#calculate()} to increase all the {@link Debt} in the same amount of all previous {@link Payment}
 *
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public class Refund extends CreditEntry {

    public Refund(final String id, final DateTime created, final LocalDate date, final String description, final BigDecimal amount) {
        super(id, created, date, description, amount);
    }

    @Override
    public boolean isForDebt() {
        return true;
    }

    @Override
    public boolean isForInterest() {
        return false;
    }

    @Override
    public boolean isForFine() {
        return false;
    }

    @Override
    public boolean isToApplyInterest() {
        return true;
    }

    @Override
    public boolean isToApplyFine() {
        return false;
    }

    /**
     * This is overridden since the amount will never be used to credit any {@link DebtEntry}
     * @return zero
     */
    @Override
    public BigDecimal getUnusedAmount() {
        return BigDecimal.ZERO;
    }
}