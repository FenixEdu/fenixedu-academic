package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SecondCycleIndividualCandidacyExemptionBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.SecondCycleIndividualCandidacyExemption;

public class CreateSecondCycleIndividualCandidacyExemption extends Service {

    public void run(final Employee employee, final SecondCycleIndividualCandidacyExemptionBean bean) {
	new SecondCycleIndividualCandidacyExemption(employee, bean.getEvent(), bean.getJustificationType());
    }
}
