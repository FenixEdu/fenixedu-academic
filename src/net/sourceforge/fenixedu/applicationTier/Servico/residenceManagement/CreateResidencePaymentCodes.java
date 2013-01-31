package net.sourceforge.fenixedu.applicationTier.Servico.residenceManagement;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import pt.ist.fenixWebFramework.services.Service;

public class CreateResidencePaymentCodes extends FenixService {

	@Service
	public static void run(List<ResidenceEvent> events) {
		for (ResidenceEvent event : events) {
			AccountingEventPaymentCode
					.create(PaymentCodeType.RESIDENCE_FEE, event.getPaymentStartDate().toYearMonthDay(), event
							.getPaymentLimitDate().toYearMonthDay(), event, event.getRoomValue(), event.getRoomValue(), event
							.getPerson());
		}
	}
}