package net.sourceforge.fenixedu.applicationTier.Servico.accounting.gratuity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreateInstallmentPenaltyExemptionBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty.InstallmentPenaltyExemption;

public class CreateInstallmentPenaltyExemption extends Service {

    public CreateInstallmentPenaltyExemption() {
	super();
    }

    public void run(final Employee employee,
	    final CreateInstallmentPenaltyExemptionBean penaltyExemptionBean) {
	for (final Installment installment : penaltyExemptionBean.getInstallments()) {
	    new InstallmentPenaltyExemption(penaltyExemptionBean.getJustificationType(),
		    penaltyExemptionBean.getGratuityEventWithPaymentPlan(), employee, installment,
		    penaltyExemptionBean.getReason(), penaltyExemptionBean
			    .getDispatchDate());
	}
    }

}
