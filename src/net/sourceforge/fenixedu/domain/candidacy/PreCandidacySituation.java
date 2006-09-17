package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;

public class PreCandidacySituation extends PreCandidacySituation_Base {

    public PreCandidacySituation(Candidacy candidacy) {
        this(candidacy, AccessControl.getUserView().getPerson());
        // TODO: create payment event
    }

    public PreCandidacySituation(Candidacy candidacy, Person person) {
        super();
        init(candidacy, person);
    }

    @Override
    public void nextState() {
        new StandByCandidacySituation(getCandidacy());
    }

    @Override
    public void checkConditionsToForward() {
        // TODO: check if the candidate has already payed emulamento
    }

    public void checkConditionsToForward(String nextState) {
        checkConditionsToForward();
    }

    public Set<String> getValidNextStates() {
        Set<String> nextStates = new HashSet<String>();
        nextStates.add(CandidacySituationType.STAND_BY.toString());
        return nextStates;
    }

    public void nextState(String nextState) {
        nextState();
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
        return CandidacySituationType.PRE_CANDIDACY;
    }

    @Override
    public boolean getCanGeneratePass() {
        return false;
    }
    
    public Collection<Operation> getOperationsForPerson(Person person) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean canExecuteOperationAutomatically() {
        // TODO Auto-generated method stub
        return false;
    }
}