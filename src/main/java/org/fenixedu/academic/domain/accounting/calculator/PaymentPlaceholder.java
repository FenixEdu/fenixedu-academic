package org.fenixedu.academic.domain.accounting.calculator;

import java.math.BigDecimal;

import org.joda.time.DateTime;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public class PaymentPlaceholder extends Payment {

    public PaymentPlaceholder(DateTime when) {
        super("zeroPayment", when, when.toLocalDate(), "placeholder payment for calculations", BigDecimal.ZERO, "");
    }
    
}