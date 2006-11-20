package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.util.PaymentCodeGenerator;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.manager.payments.PaymentsFileBean;
import net.sourceforge.fenixedu.util.sibs.SibsIncomingPaymentFile;

public class UploadSibsPaymentsFile extends Service {

    public void run(final Person person, final PaymentsFileBean paymentsFileBean)
	    throws FenixServiceException {

	final SibsIncomingPaymentFile sibsIncomingPaymentFile = SibsIncomingPaymentFile
		.parse(paymentsFileBean.getFile());

	for (final SibsIncomingPaymentFile.DetailLine detailLine : sibsIncomingPaymentFile
		.getDetailLines()) {

	    final AccountingEventPaymentCode paymentCode = getPaymentCode(detailLine);
	    paymentCode.getAccountingEvent().process(
		    person.getUser(),
		    paymentCode,
		    detailLine.getAmount(),
		    new SibsTransactionDetailDTO(detailLine.getWhenOccuredTransaction(), detailLine
			    .getSibsTransactionId(), detailLine.getCode()));

	}
    }

    private AccountingEventPaymentCode getPaymentCode(final SibsIncomingPaymentFile.DetailLine detailLine) {
	final Student student = Student.readStudentByNumber(PaymentCodeGenerator
		.getStudentNumberFrom(detailLine.getCode()));
	return (AccountingEventPaymentCode) student.getPaymentCodeBy(detailLine.getCode());
    }

}
