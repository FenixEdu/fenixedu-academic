package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class StandByConfirmedDataCandidacySituation extends StandByConfirmedDataCandidacySituation_Base {

    public StandByConfirmedDataCandidacySituation(Candidacy candidacy) {
        this(candidacy, AccessControl.getPerson());
    }

    public StandByConfirmedDataCandidacySituation(Candidacy candidacy, Person person) {
	super();
        init(candidacy, person);
	}

    @Override
    public void checkConditionsToForward() {
	// TODO Auto-generated method stub

    }

    @Override
    public void nextState() {
	// TODO Auto-generated method stub

    }

    @Override
    public void checkConditionsToForward(String nextState) {
	// TODO Auto-generated method stub

    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
	return CandidacySituationType.STAND_BY_CONFIRMED_DATA;
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
	    new SubstituteCandidacySituation(this.getCandidacy());
	    break;
	default:
	    break;
	}

    }

    public Collection<Operation> getOperationsForPerson(Person person) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public boolean canExecuteOperationAutomatically() {
        return false;
    }
}