package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;

public class StandByFilledDataCandidacySituation extends StandByFilledDataCandidacySituation_Base {

    public StandByFilledDataCandidacySituation(Candidacy candidacy) {
        this(candidacy, AccessControl.getUserView().getPerson());
    }

    public StandByFilledDataCandidacySituation(Candidacy candidacy, Person person) {
	super();
        init(candidacy, person);
    }

    @Override
    public void checkConditionsToForward() {

    }

    @Override
    public void nextState() {
	new AdmittedCandidacySituation(getCandidacy());
    }

    @Override
    public boolean canChangePersonalData() {
	return true;
    }

    @Override
    public void checkConditionsToForward(String nextState) {
	checkConditionsToForward();
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
	return CandidacySituationType.STAND_BY_FILLED_DATA;
    }

    @Override
    public Set<String> getValidNextStates() {
	Set<String> nextStates = new HashSet<String>();
	nextStates.add(CandidacySituationType.STAND_BY_CONFIRMED_DATA.toString());
	nextStates.add(CandidacySituationType.STAND_BY.toString());
	return nextStates;
    }

    @Override
    public void nextState(String nextState) {
	CandidacySituationType nextStateType = CandidacySituationType.valueOf(nextState);
	switch (nextStateType) {
	case STAND_BY_CONFIRMED_DATA:
	    new StandByConfirmedDataCandidacySituation(getCandidacy());
	    break;
	case STAND_BY:
	    new StandByCandidacySituation(getCandidacy());
	    break;
	default:
	    throw new DomainException("invalid.next.state");
	}
    }

    @Override
    public boolean getCanCandidacyDataBeValidated() {
	return true;
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