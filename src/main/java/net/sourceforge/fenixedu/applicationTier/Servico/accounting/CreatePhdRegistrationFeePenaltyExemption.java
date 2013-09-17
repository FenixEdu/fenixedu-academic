package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreatePhdRegistrationFeePenaltyExemptionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.phd.debts.PhdRegistrationFeePenaltyExemption;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import pt.ist.fenixframework.Atomic;

public class CreatePhdRegistrationFeePenaltyExemption {

    @Atomic
    public static void run(final Person responsible, final CreatePhdRegistrationFeePenaltyExemptionBean penaltyExemptionBean) {
        check(AcademicPredicates.MANAGE_STUDENT_PAYMENTS);
        new PhdRegistrationFeePenaltyExemption(penaltyExemptionBean.getJustificationType(), penaltyExemptionBean.getEvent(),
                responsible, penaltyExemptionBean.getReason(), penaltyExemptionBean.getDispatchDate());
    }

}