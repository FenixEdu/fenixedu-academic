package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsurancePenaltyExemption;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateAdministrativeOfficeFeeAndInsurancePenaltyExemption extends FenixService {

    @Checked("RolePredicates.ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static void run(final Employee employee,
	    final CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean penaltyExemptionBean) {

	new AdministrativeOfficeFeeAndInsurancePenaltyExemption(penaltyExemptionBean.getJustificationType(),
		(AdministrativeOfficeFeeAndInsuranceEvent) penaltyExemptionBean.getEvent(), employee, penaltyExemptionBean
			.getReason(), penaltyExemptionBean.getDispatchDate());

    }

}