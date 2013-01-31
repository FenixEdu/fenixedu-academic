package net.sourceforge.fenixedu.applicationTier.Servico.accounting.gratuity;

import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreateInstallmentPenaltyExemptionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty.InstallmentPenaltyExemption;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateInstallmentPenaltyExemption {

	@Checked("AcademicPredicates.MANAGE_STUDENT_PAYMENTS")
	@Service
	public static void run(final Person responsible, final CreateInstallmentPenaltyExemptionBean penaltyExemptionBean) {
		for (final Installment installment : penaltyExemptionBean.getInstallments()) {
			new InstallmentPenaltyExemption(penaltyExemptionBean.getJustificationType(),
					penaltyExemptionBean.getGratuityEventWithPaymentPlan(), responsible, installment,
					penaltyExemptionBean.getReason(), penaltyExemptionBean.getDispatchDate());
		}
	}

}