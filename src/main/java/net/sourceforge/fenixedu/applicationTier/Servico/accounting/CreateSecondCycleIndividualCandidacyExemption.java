package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.dataTransferObject.accounting.SecondCycleIndividualCandidacyExemptionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.SecondCycleIndividualCandidacyExemption;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;
import pt.ist.fenixframework.Atomic;

public class CreateSecondCycleIndividualCandidacyExemption {

    @Atomic
    public static void run(final Person responsible, final SecondCycleIndividualCandidacyExemptionBean bean) {
        check(AcademicPredicates.MANAGE_STUDENT_PAYMENTS);
        new SecondCycleIndividualCandidacyExemption(responsible, bean.getEvent(), bean.getJustificationType());
    }
}