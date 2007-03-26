package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsurancePenaltyExemption;

public class CreateAdministrativeOfficeFeeAndInsurancePenaltyExemption extends Service {

    public CreateAdministrativeOfficeFeeAndInsurancePenaltyExemption() {
	super();
    }

    public void run(final Employee employee,
	    final CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean penaltyExemptionBean) {

	new AdministrativeOfficeFeeAndInsurancePenaltyExemption(penaltyExemptionBean.getJustificationType(),
		(AdministrativeOfficeFeeAndInsuranceEvent) penaltyExemptionBean.getEvent(), employee,
		penaltyExemptionBean.getReason(), penaltyExemptionBean
			.getDispatchDate());

    }

}
