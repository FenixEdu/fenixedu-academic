package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SecondCycleIndividualCandidacyExemptionBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.SecondCycleIndividualCandidacyExemption;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateSecondCycleIndividualCandidacyExemption extends FenixService {

    @Checked("RolePredicates.ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static void run(final Employee employee, final SecondCycleIndividualCandidacyExemptionBean bean) {
	new SecondCycleIndividualCandidacyExemption(employee, bean.getEvent(), bean.getJustificationType());
    }
}