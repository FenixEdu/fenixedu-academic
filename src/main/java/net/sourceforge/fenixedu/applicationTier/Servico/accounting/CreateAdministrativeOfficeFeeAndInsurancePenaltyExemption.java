package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsurancePenaltyExemption;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import pt.ist.fenixframework.Atomic;

public class CreateAdministrativeOfficeFeeAndInsurancePenaltyExemption {

    @Atomic
    public static void run(final Person responsible,
            final CreateAdministrativeOfficeFeeAndInsurancePenaltyExemptionBean penaltyExemptionBean) {
        check(AcademicPredicates.MANAGE_STUDENT_PAYMENTS);

        new AdministrativeOfficeFeeAndInsurancePenaltyExemption(penaltyExemptionBean.getJustificationType(),
                (AdministrativeOfficeFeeAndInsuranceEvent) penaltyExemptionBean.getEvent(), responsible,
                penaltyExemptionBean.getReason(), penaltyExemptionBean.getDispatchDate());

    }

}