package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collection;

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
    public CandidacySituationType getCandidacySituationType() {
	return CandidacySituationType.STAND_BY_CONFIRMED_DATA;
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