package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Set;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class StandByConfirmedDataCandidacySituation extends StandByConfirmedDataCandidacySituation_Base {
    
    public  StandByConfirmedDataCandidacySituation(Candidacy candidacy) {
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextState() {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void checkConditionsToForward(String nextState) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
        return CandidacySituationType.STAND_BY_CONFIRMED_DATA;
    }

    @Override
    public Set<String> getValidNextStates() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void nextState(String nextState) {
        // TODO Auto-generated method stub
        
    }
    
}
