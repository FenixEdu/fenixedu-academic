package net.sourceforge.fenixedu.domain.candidacy;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CancelledCandidacySituation extends CancelledCandidacySituation_Base {

    public CancelledCandidacySituation(Candidacy candidacy, Person person) {
	super();
	super.init(candidacy, person);
    }

    public CancelledCandidacySituation(Candidacy candidacy) {
	this(candidacy, (AccessControl.getUserView() != null) ? AccessControl.getUserView().getPerson()
		: null);
    }

    @Override
    public void checkConditionsToForward() {
	throw new DomainException("error.impossible.to.forward.from.cancelled");
    }

    @Override
    public void checkConditionsToForward(String nextState) {
	throw new DomainException("error.impossible.to.forward.from.cancelled");
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
	return CandidacySituationType.CANCELLED;
    }

    @Override
    public Set<String> getValidNextStates() {
	return new HashSet<String>();
    }

    @Override
    public void nextState() {
	throw new DomainException("error.impossible.to.forward.from.cancelled");
    }

    @Override
    public void nextState(String nextState) {
	throw new DomainException("error.impossible.to.forward.from.cancelled");
    }

    @Override
    public boolean canExecuteOperationAutomatically() {
	return false;
    }

}
