package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class StandByFilledDataCandidacySituation extends StandByFilledDataCandidacySituation_Base {

    public StandByFilledDataCandidacySituation(Candidacy candidacy) {
	this(candidacy, AccessControl.getPerson());
    }

    public StandByFilledDataCandidacySituation(Candidacy candidacy, Person person) {
	super();
	init(candidacy, person);
    }

    @Override
    public boolean canChangePersonalData() {
	return true;
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
	return CandidacySituationType.STAND_BY_FILLED_DATA;
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