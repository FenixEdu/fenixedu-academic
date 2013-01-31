package net.sourceforge.fenixedu.domain.accounting.installments;

import net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan.InstallmentBean;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import pt.ist.fenixWebFramework.services.Service;

public class InstallmentService {

	@Service
	public static Installment edit(final Installment installment, final InstallmentBean bean) {
		installment.edit(bean);
		return installment;
	}

}
