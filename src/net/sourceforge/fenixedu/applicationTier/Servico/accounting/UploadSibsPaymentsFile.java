package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import pt.utl.ist.fenix.tools.util.FileUtils;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.util.PaymentCodeGenerator;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.manager.payments.PaymentsFileBean;
import net.sourceforge.fenixedu.util.sibs.incomming.SibsIncommingPaymentFile;
import net.sourceforge.fenixedu.util.sibs.incomming.SibsIncommingPaymentFileDetailLine;

public class UploadSibsPaymentsFile extends Service {

    public void run(final Person person, final PaymentsFileBean paymentsFileBean)
	    throws FenixServiceException {

	final SibsIncommingPaymentFile sibsIncomingPaymentFile = SibsIncommingPaymentFile.parse(
		FileUtils.getFilenameOnly(paymentsFileBean.getFilename()), paymentsFileBean.getFile());
	for (final SibsIncommingPaymentFileDetailLine detailLine : sibsIncomingPaymentFile
		.getDetailLines()) {
	    getPaymentCode(detailLine).process(person, detailLine.getAmount(),
		    detailLine.getWhenOccuredTransaction(), detailLine.getSibsTransactionId());
	}
    }

    private PaymentCode getPaymentCode(final SibsIncommingPaymentFileDetailLine detailLine) {
	final Student student = Student.readStudentByNumber(PaymentCodeGenerator
		.getStudentNumberFrom(detailLine.getCode()));
	return student.getPaymentCodeBy(detailLine.getCode());
    }

}
