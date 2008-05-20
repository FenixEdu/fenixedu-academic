package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;

public class Over23IndividualCandidacyResultBean implements Serializable {

    private DomainReference<Over23IndividualCandidacyProcess> candidacyProcess;

    private IndividualCandidacyState state;

    private DomainReference<Degree> acceptedDegree;

    public Over23IndividualCandidacyResultBean(final Over23IndividualCandidacyProcess candidacyProcess) {
	setCandidacyProcess(candidacyProcess);
	if (candidacyProcess.isCandidacyAccepted()) {
	    setState(candidacyProcess.getCandidacyState());
	    setAcceptedDegree(candidacyProcess.getAcceptedDegree());
	} else if (candidacyProcess.isCandidacyRejected()) {
	    setState(candidacyProcess.getCandidacyState());
	}
    }

    public Over23IndividualCandidacyProcess getCandidacyProcess() {
	return (this.candidacyProcess != null) ? this.candidacyProcess.getObject() : null;
    }

    public void setCandidacyProcess(final Over23IndividualCandidacyProcess candidacyProcess) {
	this.candidacyProcess = (candidacyProcess != null) ? new DomainReference<Over23IndividualCandidacyProcess>(
		candidacyProcess) : null;
    }

    public IndividualCandidacyState getState() {
	return state;
    }

    public void setState(IndividualCandidacyState state) {
	this.state = state;
    }

    public Degree getAcceptedDegree() {
	return (this.acceptedDegree != null) ? this.acceptedDegree.getObject() : null;
    }

    public void setAcceptedDegree(final Degree acceptedDegree) {
	this.acceptedDegree = (acceptedDegree != null) ? new DomainReference<Degree>(acceptedDegree) : null;
    }

    public boolean isValid() {
	return getState() != null;
    }
}