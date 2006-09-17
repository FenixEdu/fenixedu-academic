package net.sourceforge.fenixedu.domain.candidacy;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.accessControl.AccessControl;

public class SubstituteCandidacySituation extends SubstituteCandidacySituation_Base {

    public SubstituteCandidacySituation(Candidacy candidacy) {
	super();
	init(candidacy, AccessControl.getUserView().getPerson());
    }

    @Override
    public void checkConditionsToForward() {
    }

    @Override
    public void checkConditionsToForward(String nextState) {
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
	return CandidacySituationType.SUBSTITUTE;
    }

    @Override
    public Set<String> getValidNextStates() {
	Set<String> nextStates = new HashSet<String>();
	nextStates.add(CandidacySituationType.ADMITTED.toString());
	nextStates.add(CandidacySituationType.NOT_ADMITTED.toString());
	nextStates.add(CandidacySituationType.SUBSTITUTE.toString());
	return nextStates;
    }

    @Override
    public void nextState() {
	// TODO Auto-generated method stub

    }

    @Override
    public void nextState(String nextState) {
	CandidacySituationType situationType = CandidacySituationType.valueOf(nextState);
	switch (situationType) {
	case ADMITTED:
	    new AdmittedCandidacySituation(this.getCandidacy());
	    break;
	case NOT_ADMITTED:
	    new NotAdmittedCandidacySituation(this.getCandidacy());
	    break;
	case SUBSTITUTE:
	    break;
	default:
	    break;
	}
    }
    
    @Override
    public boolean canExecuteOperationAutomatically() {
	// TODO Auto-generated method stub
	return false;
    }

}
