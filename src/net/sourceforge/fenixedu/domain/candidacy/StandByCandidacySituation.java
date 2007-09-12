package net.sourceforge.fenixedu.domain.candidacy;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class StandByCandidacySituation extends StandByCandidacySituation_Base {

    public StandByCandidacySituation(Candidacy candidacy) {
	this(candidacy, AccessControl.getPerson());
    }

    public StandByCandidacySituation(Candidacy candidacy, Person person) {
	super();
	init(candidacy, person);
    }

    @Override
    public boolean canChangePersonalData() {
	return true;
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
	return CandidacySituationType.STAND_BY;
    }

    @Override
    public boolean getCanCandidacyDataBeValidated() {
	return true;
    }

    @Override
    public boolean canExecuteOperationAutomatically() {
	return false;
    }
}