package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsurancePenaltyExemption;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class CreateAdministrativeOfficeFeeAndInsurancePenaltyExemption {

    @Checked("AcademicPredicates.MANAGE_STUDENT_PAYMENTS")
    @Atomic
    public static void run(final Person responsible,
            final CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean penaltyExemptionBean) {

        new AdministrativeOfficeFeeAndInsurancePenaltyExemption(penaltyExemptionBean.getJustificationType(),
                (AdministrativeOfficeFeeAndInsuranceEvent) penaltyExemptionBean.getEvent(), responsible,
                penaltyExemptionBean.getReason(), penaltyExemptionBean.getDispatchDate());

    }

}