package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.CreateOtherPartyPaymentBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;

import org.joda.time.DateTime;

public class CreateOtherPartyPayment extends Service {

    public CreateOtherPartyPayment() {
	super();
    }

    public void run(final Person person, final CreateOtherPartyPaymentBean createOtherPartyPaymentBean) {
	final Event event = createOtherPartyPaymentBean.getEvent();
	event.addOtherPartyAmount(person.getUser(), createOtherPartyPaymentBean.getContributorParty(),
		createOtherPartyPaymentBean.getAmount(), new AccountingTransactionDetailDTO(
			new DateTime(), PaymentMode.CASH));
    }

}