package net.sourceforge.fenixedu.domain.candidacy;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class NotAdmittedCandidacySituation extends NotAdmittedCandidacySituation_Base {

    public NotAdmittedCandidacySituation(Candidacy candidacy) {
        super();
        init(candidacy, AccessControl.getUserView().getPerson());
    }

    @Override
    public void checkConditionsToForward() {
	throw new DomainException("error.impossible.to.forward.from.notAdmitted");
    }

    @Override
    public void checkConditionsToForward(String nextState) {
	throw new DomainException("error.impossible.to.forward.from.notAdmitted");
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
	return CandidacySituationType.NOT_ADMITTED;
    }

    @Override
    public Set<String> getValidNextStates() {
	return new HashSet<String>();
    }

    @Override
    public void nextState() {
	throw new DomainException("error.impossible.to.forward.from.notAdmitted");
    }

    @Override
    public void nextState(String nextState) {
	throw new DomainException("error.impossible.to.forward.from.notAdmitted");
    }
    
    @Override
    public boolean canExecuteOperationAutomatically() {
	return false;
    }

}
