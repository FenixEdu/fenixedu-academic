package net.sourceforge.fenixedu.domain.candidacyProcess.standalone;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;

public class StandaloneIndividualCandidacyResultBean implements Serializable {

    private DomainReference<StandaloneIndividualCandidacyProcess> candidacyProcess;
    private IndividualCandidacyState state;

    public StandaloneIndividualCandidacyResultBean(final StandaloneIndividualCandidacyProcess process) {
	setCandidacyProcess(process);
	if (process.isCandidacyAccepted() || process.isCandidacyRejected()) {
	    setState(process.getCandidacyState());
	}
    }

    public StandaloneIndividualCandidacyProcess getCandidacyProcess() {
	return (this.candidacyProcess != null) ? this.candidacyProcess.getObject() : null;
    }

    public void setCandidacyProcess(StandaloneIndividualCandidacyProcess candidacyProcess) {
	this.candidacyProcess = (candidacyProcess != null) ? new DomainReference<StandaloneIndividualCandidacyProcess>(
		candidacyProcess) : null;
    }

    public IndividualCandidacyState getState() {
	return state;
    }

    public void setState(IndividualCandidacyState state) {
	this.state = state;
    }
}
