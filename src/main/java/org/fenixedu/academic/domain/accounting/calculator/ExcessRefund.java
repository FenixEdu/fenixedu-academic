package org.fenixedu.academic.domain.accounting.calculator;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public class ExcessRefund extends DebtEntry {

    private String targetPaymentId;

    public ExcessRefund(final String id, final DateTime created, final LocalDate date, final String description,
            final BigDecimal amount, String targetPaymentId) {
        super(id, created, date, description, amount);
        setTargetPaymentId(targetPaymentId);
    }

    @Override
    boolean isToDeposit(final CreditEntry creditEntry) {
        return creditEntry instanceof Payment;
    }

    public String getTargetPaymentId() {
        return targetPaymentId;
    }

    public void setTargetPaymentId(final String targetPaymentId) {
        this.targetPaymentId = targetPaymentId;
    }
}
