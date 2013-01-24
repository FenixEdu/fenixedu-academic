package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreatePhdRegistrationFeePenaltyExemptionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.phd.debts.PhdRegistrationFeePenaltyExemption;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreatePhdRegistrationFeePenaltyExemption {

    @Service
    @Checked("AcademicPredicates.MANAGE_STUDENT_PAYMENTS")
    public static void run(final Person responsible, final CreatePhdRegistrationFeePenaltyExemptionBean penaltyExemptionBean) {
	new PhdRegistrationFeePenaltyExemption(penaltyExemptionBean.getJustificationType(), penaltyExemptionBean.getEvent(),
		responsible, penaltyExemptionBean.getReason(), penaltyExemptionBean.getDispatchDate());
    }

}