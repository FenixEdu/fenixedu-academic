package net.sourceforge.fenixedu.domain.candidacy;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.Person;

public class AdmittedCandidacySituation extends AdmittedCandidacySituation_Base {

    public AdmittedCandidacySituation(Candidacy candidacy) {
	this(candidacy, AccessControl.getUserView().getPerson());
    }

    public AdmittedCandidacySituation(Candidacy candidacy, Person person) {
	super();
	init(candidacy, person);
    }

    @Override
    public void checkConditionsToForward() {
    }

    @Override
    public void checkConditionsToForward(String nextState) {
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
	return CandidacySituationType.ADMITTED;
    }

    @Override
    public Set<String> getValidNextStates() {
	Set<String> nextStates = new HashSet<String>();
	nextStates.add(CandidacySituationType.REGISTERED.toString());
	nextStates.add(CandidacySituationType.ADMITTED.toString());
	nextStates.add(CandidacySituationType.NOT_ADMITTED.toString());
	nextStates.add(CandidacySituationType.SUBSTITUTE.toString());
	return nextStates;
    }

    @Override
    public void nextState() {
	new RegisteredCandidacySituation(this.getCandidacy());
    }

    @Override
    public void nextState(String nextState) {
	CandidacySituationType situationType = CandidacySituationType.valueOf(nextState);
	switch (situationType) {
	case REGISTERED:
	    new RegisteredCandidacySituation(this.getCandidacy());
	    break;
	case ADMITTED:
	    break;
	case NOT_ADMITTED:
	    new NotAdmittedCandidacySituation(this.getCandidacy());
	    break;
	case SUBSTITUTE:
	    new SubstituteCandidacySituation(this.getCandidacy());
	    break;
	default:
	    break;
	}
    }

    @Override
    public boolean getCanRegister() {
	return true;
    }

    @Override
    public boolean canExecuteOperationAutomatically() {
	if (getCandidacy() instanceof DegreeCandidacy) {
	    return true;
	}
	return false;
    }

}
