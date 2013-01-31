package net.sourceforge.fenixedu.domain.candidacyProcess.standalone;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;

public class StandaloneIndividualCandidacyResultBean implements Serializable {

	private StandaloneIndividualCandidacyProcess candidacyProcess;
	private IndividualCandidacyState state;

	public StandaloneIndividualCandidacyResultBean(final StandaloneIndividualCandidacyProcess process) {
		setCandidacyProcess(process);
		if (process.isCandidacyAccepted() || process.isCandidacyRejected()) {
			setState(process.getCandidacyState());
		}
	}

	public StandaloneIndividualCandidacyProcess getCandidacyProcess() {
		return this.candidacyProcess;
	}

	public void setCandidacyProcess(StandaloneIndividualCandidacyProcess candidacyProcess) {
		this.candidacyProcess = candidacyProcess;
	}

	public IndividualCandidacyState getState() {
		return state;
	}

	public void setState(IndividualCandidacyState state) {
		this.state = state;
	}
}
