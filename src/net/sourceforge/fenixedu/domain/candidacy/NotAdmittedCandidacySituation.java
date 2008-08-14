package net.sourceforge.fenixedu.domain.candidacy;

import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class NotAdmittedCandidacySituation extends NotAdmittedCandidacySituation_Base {

    public NotAdmittedCandidacySituation(Candidacy candidacy) {
	super();
	init(candidacy, AccessControl.getPerson());
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
	return CandidacySituationType.NOT_ADMITTED;
    }

    @Override
    public boolean canExecuteOperationAutomatically() {
	return false;
    }

}
