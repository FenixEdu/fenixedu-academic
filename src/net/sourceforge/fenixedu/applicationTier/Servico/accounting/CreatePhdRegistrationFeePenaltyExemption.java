package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreatePhdRegistrationFeePenaltyExemptionBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.phd.debts.PhdRegistrationFeePenaltyExemption;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreatePhdRegistrationFeePenaltyExemption {

    @Service
    @Checked("RolePredicates.ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    public static void run(final Employee employee, final CreatePhdRegistrationFeePenaltyExemptionBean penaltyExemptionBean) {
	new PhdRegistrationFeePenaltyExemption(penaltyExemptionBean.getJustificationType(), penaltyExemptionBean.getEvent(),
		employee, penaltyExemptionBean.getReason(), penaltyExemptionBean.getDispatchDate());
    }

}