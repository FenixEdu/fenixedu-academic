package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreateImprovementOfApprovedEnrolmentPenaltyExemptionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.ImprovementOfApprovedEnrolmentEvent;
import net.sourceforge.fenixedu.domain.accounting.events.ImprovementOfApprovedEnrolmentPenaltyExemption;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import pt.ist.fenixframework.Atomic;

public class CreateImprovementOfApprovedEnrolmentPenaltyExemption {

    @Atomic
    public static void run(final Person responsible,
            final CreateImprovementOfApprovedEnrolmentPenaltyExemptionBean penaltyExemptionBean) {
        check(AcademicPredicates.MANAGE_STUDENT_PAYMENTS);

        new ImprovementOfApprovedEnrolmentPenaltyExemption(penaltyExemptionBean.getJustificationType(),
                (ImprovementOfApprovedEnrolmentEvent) penaltyExemptionBean.getEvent(), responsible,
                penaltyExemptionBean.getReason(), penaltyExemptionBean.getDispatchDate());
    }

}