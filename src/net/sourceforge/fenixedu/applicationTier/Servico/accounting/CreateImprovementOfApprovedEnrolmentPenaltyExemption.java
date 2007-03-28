package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreateImprovementOfApprovedEnrolmentPenaltyExemptionBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.events.ImprovementOfApprovedEnrolmentEvent;
import net.sourceforge.fenixedu.domain.accounting.events.ImprovementOfApprovedEnrolmentPenaltyExemption;

public class CreateImprovementOfApprovedEnrolmentPenaltyExemption extends Service {

    public CreateImprovementOfApprovedEnrolmentPenaltyExemption() {
	super();
    }

    public void run(final Employee employee, final CreateImprovementOfApprovedEnrolmentPenaltyExemptionBean penaltyExemptionBean) {

	new ImprovementOfApprovedEnrolmentPenaltyExemption(
		penaltyExemptionBean.getJustificationType(),
		(ImprovementOfApprovedEnrolmentEvent) penaltyExemptionBean.getEvent(), 
		employee,
		penaltyExemptionBean.getReason(),
		penaltyExemptionBean.getDispatchDate());
    }

}
