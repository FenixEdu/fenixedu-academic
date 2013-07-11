package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption.CreateImprovementOfApprovedEnrolmentPenaltyExemptionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.ImprovementOfApprovedEnrolmentEvent;
import net.sourceforge.fenixedu.domain.accounting.events.ImprovementOfApprovedEnrolmentPenaltyExemption;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class CreateImprovementOfApprovedEnrolmentPenaltyExemption {

    @Checked("AcademicPredicates.MANAGE_STUDENT_PAYMENTS")
    @Atomic
    public static void run(final Person responsible,
            final CreateImprovementOfApprovedEnrolmentPenaltyExemptionBean penaltyExemptionBean) {

        new ImprovementOfApprovedEnrolmentPenaltyExemption(penaltyExemptionBean.getJustificationType(),
                (ImprovementOfApprovedEnrolmentEvent) penaltyExemptionBean.getEvent(), responsible,
                penaltyExemptionBean.getReason(), penaltyExemptionBean.getDispatchDate());
    }

}