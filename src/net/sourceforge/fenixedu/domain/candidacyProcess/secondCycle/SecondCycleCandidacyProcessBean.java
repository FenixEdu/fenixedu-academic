package net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;

public class SecondCycleCandidacyProcessBean extends CandidacyProcessBean {
    
    public SecondCycleCandidacyProcessBean() {
    }

    public SecondCycleCandidacyProcessBean(final ExecutionInterval executionInterval) {
	setExecutionInterval(executionInterval);
    }

    public SecondCycleCandidacyProcessBean(final SecondCycleCandidacyProcess process) {
	setExecutionInterval(process.getCandidacyExecutionInterval());
	setStart(process.getCandidacyStart());
	setEnd(process.getCandidacyEnd());
    }

}
