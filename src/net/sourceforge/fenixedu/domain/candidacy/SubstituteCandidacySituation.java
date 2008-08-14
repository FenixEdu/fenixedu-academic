package net.sourceforge.fenixedu.domain.candidacy;

import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class SubstituteCandidacySituation extends SubstituteCandidacySituation_Base {

    public SubstituteCandidacySituation(Candidacy candidacy) {
	super();
	init(candidacy, AccessControl.getPerson());
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
	return CandidacySituationType.SUBSTITUTE;
    }

    @Override
    public boolean canExecuteOperationAutomatically() {
	// TODO Auto-generated method stub
	return false;
    }

}
