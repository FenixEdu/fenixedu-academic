package net.sourceforge.fenixedu.applicationTier.Servico.residenceManagement;


import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class PayResidenceEvent {

    @Atomic
    public static void run(User user, ResidenceEvent event, YearMonthDay date) {
        event.process(user, event.calculateEntries(), new AccountingTransactionDetailDTO(date.toDateTimeAtMidnight(),
                PaymentMode.CASH));
    }
}