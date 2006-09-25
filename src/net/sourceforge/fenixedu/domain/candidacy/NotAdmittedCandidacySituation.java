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
	//throw new DomainException("error.impossible.to.forward.from.notAdmitted");
    }

    @Override
    public void checkConditionsToForward(String nextState) {
	//throw new DomainException("error.impossible.to.forward.from.notAdmitted");
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
	return CandidacySituationType.NOT_ADMITTED;
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
	//throw new DomainException("error.impossible.to.forward.from.notAdmitted");
    }

    @Override
    public void nextState(String nextState) {
    CandidacySituationType situationType = CandidacySituationType.valueOf(nextState);
    switch (situationType) {
    case ADMITTED:
        new AdmittedCandidacySituation(this.getCandidacy());
        break;
    case NOT_ADMITTED:
        break;
    case SUBSTITUTE:
        new SubstituteCandidacySituation(this.getCandidacy());
        break;
    default:
        break;
    }
    }
    
    @Override
    public boolean canExecuteOperationAutomatically() {
	return false;
    }

}
