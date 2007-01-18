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
	    final CreateInstallmentPenaltyExemptionBean createInstallmentPenaltyExemptionBean) {
	for (final Installment installment : createInstallmentPenaltyExemptionBean.getInstallments()) {
	    new InstallmentPenaltyExemption(createInstallmentPenaltyExemptionBean.getExemptionType(),
		    createInstallmentPenaltyExemptionBean.getGratuityEventWithPaymentPlan(), employee,
		    installment, createInstallmentPenaltyExemptionBean.getComments());
	}
    }

}
