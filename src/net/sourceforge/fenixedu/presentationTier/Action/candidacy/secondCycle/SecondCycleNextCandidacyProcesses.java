package net.sourceforge.fenixedu.presentationTier.Action.candidacy.secondCycle;

import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.presentationTier.renderers.providers.AbstractDomainObjectProvider;

public class SecondCycleNextCandidacyProcesses extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
	SecondCycleIndividualCandidacyProcessBean bean = (SecondCycleIndividualCandidacyProcessBean) source;

	SecondCycleCandidacyProcess process = bean.getCandidacyProcess();

	return process.getNextSecondCyleCandidacyProcesses();
    }

}
