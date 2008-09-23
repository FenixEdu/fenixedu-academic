package net.sourceforge.fenixedu.applicationTier.Servico.residenceManagement;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;

public class PayResidenceEvent extends FenixService {

    public void run(User user, ResidenceEvent event) {
	event.process(user, event.calculateEntries(), new AccountingTransactionDetailDTO(new DateTime(), PaymentMode.CASH));
    }
}
