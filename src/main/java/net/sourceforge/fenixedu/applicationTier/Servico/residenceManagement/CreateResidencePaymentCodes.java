package net.sourceforge.fenixedu.applicationTier.Servico.residenceManagement;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import pt.ist.fenixframework.Atomic;

public class CreateResidencePaymentCodes {

    @Atomic
    public static void run(Collection<ResidenceEvent> events) {
        for (ResidenceEvent event : events) {
            AccountingEventPaymentCode
                    .create(PaymentCodeType.RESIDENCE_FEE, event.getPaymentStartDate().toYearMonthDay(), event
                            .getPaymentLimitDate().toYearMonthDay(), event, event.getRoomValue(), event.getRoomValue(), event
                            .getPerson());
        }
    }
}