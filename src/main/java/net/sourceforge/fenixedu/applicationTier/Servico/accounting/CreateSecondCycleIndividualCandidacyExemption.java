package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.dataTransferObject.accounting.SecondCycleIndividualCandidacyExemptionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.SecondCycleIndividualCandidacyExemption;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class CreateSecondCycleIndividualCandidacyExemption {

    @Checked("AcademicPredicates.MANAGE_STUDENT_PAYMENTS")
    @Atomic
    public static void run(final Person responsible, final SecondCycleIndividualCandidacyExemptionBean bean) {
        new SecondCycleIndividualCandidacyExemption(responsible, bean.getEvent(), bean.getJustificationType());
    }
}