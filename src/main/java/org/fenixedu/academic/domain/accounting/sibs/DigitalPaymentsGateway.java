package org.fenixedu.academic.domain.accounting.sibs;

import org.fenixedu.academic.domain.accounting.Event;
import pt.ist.fenixframework.Atomic;
import pt.ist.payments.bean.SibsPaymentBean;
import pt.ist.payments.domain.SibsPayment;
import pt.ist.payments.domain.exception.SibsPaymentException;

public class DigitalPaymentsGateway {

    @Atomic
    public static SibsPayment generateSibsPayment(final Event event, SibsPaymentBean sibsPaymentBean) throws SibsPaymentException {
        final SibsPayment sibsPayment = SibsPayment.generate(sibsPaymentBean);
        sibsPayment.setEvent(event);
        return sibsPayment;
    }

}
