package net.sourceforge.fenixedu.domain.candidacy;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class StandByCandidacySituation extends StandByCandidacySituation_Base {
    
    public  StandByCandidacySituation(Candidacy candidacy) {
        super();
        setCandidacy(candidacy);
        Employee employee = AccessControl.getUserView().getPerson().getEmployee();
        if(employee == null) {
        	throw new DomainException("person is not an employee");
        }
        setEmployee(employee);
    }

	@Override
	public void checkConditionsToForward() {
		// TODO check if all necessary data is filled
		if(!checkIfDataIsFilled()) {
			throw new DomainException("");
		}
	}

	@Override
	public void nextState() {
		new StandByFilledDataCandidacySituation(getCandidacy());
	}
	
	private boolean checkIfDataIsFilled() {
		return false;
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
        return CandidacySituationType.STAND_BY;
    }

    @Override
    public Set<String> getValidNextStates() {
        Set<String> nextStates = new HashSet<String>();
        nextStates.add(CandidacySituationType.STAND_BY_FILLED_DATA.toString());
        return nextStates;
    }

    @Override
    public void nextState(String nextState) {
        nextState();
    }
    
}
