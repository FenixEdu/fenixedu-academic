package net.sourceforge.fenixedu.applicationTier.Servico.accounting.gratuity;

import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreateInstallmentPenaltyExemptionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.exemption.penalty.InstallmentPenaltyExemption;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import pt.ist.fenixframework.Atomic;

public class CreateInstallmentPenaltyExemption {

    @Atomic
    public static void run(final Person responsible, final CreateInstallmentPenaltyExemptionBean penaltyExemptionBean) {
        check(AcademicPredicates.MANAGE_STUDENT_PAYMENTS);
        for (final Installment installment : penaltyExemptionBean.getInstallments()) {
            new InstallmentPenaltyExemption(penaltyExemptionBean.getJustificationType(),
                    penaltyExemptionBean.getGratuityEventWithPaymentPlan(), responsible, installment,
                    penaltyExemptionBean.getReason(), penaltyExemptionBean.getDispatchDate());
        }
    }

}