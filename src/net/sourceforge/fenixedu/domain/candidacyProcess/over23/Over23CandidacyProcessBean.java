package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;

public class Over23CandidacyProcessBean extends CandidacyProcessBean {

    public Over23CandidacyProcessBean() {
    }

    public Over23CandidacyProcessBean(final ExecutionInterval executionInterval) {
	setExecutionInterval(executionInterval);
    }

    public Over23CandidacyProcessBean(final Over23CandidacyProcess process) {
	setExecutionInterval(process.getCandidacyExecutionInterval());
	setStart(process.getCandidacyStart());
	setEnd(process.getCandidacyEnd());
    }

}
