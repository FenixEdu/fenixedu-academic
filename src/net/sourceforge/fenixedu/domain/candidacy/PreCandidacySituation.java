package net.sourceforge.fenixedu.domain.candidacy;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PreCandidacySituation extends PreCandidacySituation_Base {

    public PreCandidacySituation(Candidacy candidacy) {
        super();
        setCandidacy(candidacy);
        Employee employee = AccessControl.getUserView().getPerson().getEmployee();
        if (employee == null) {
            throw new DomainException("person is not an employee");
        }
        setEmployee(employee);
        // TODO: create payment event
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

}
