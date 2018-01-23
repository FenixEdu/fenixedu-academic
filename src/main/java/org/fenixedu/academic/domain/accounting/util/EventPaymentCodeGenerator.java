package org.fenixedu.academic.domain.accounting.util;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.domain.accounting.PaymentCodeType;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public class EventPaymentCodeGenerator extends PaymentCodeGenerator {
    @Override
    public boolean canGenerateNewCode(PaymentCodeType paymentCodeType, Person person) {
        return paymentCodeType == PaymentCodeType.EVENT;
    }

    @Override
    @Atomic(mode = Atomic.TxMode.WRITE)
    public String generateNewCodeFor(PaymentCodeType paymentCodeType, Person person) {
        return Bennu.getInstance().getPaymentCodePool().generateNewCode();
    }

    @Override
    public boolean isCodeMadeByThisFactory(PaymentCode paymentCode) {
        return paymentCode.getType() == PaymentCodeType.EVENT;
    }
}
