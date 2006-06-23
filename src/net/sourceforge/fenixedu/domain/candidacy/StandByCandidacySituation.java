package net.sourceforge.fenixedu.domain.candidacy;

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
		CandidacySituation candidacySituation = new StandByFilledDataCandidacySituation(getCandidacy());
		
	}
	
	private boolean checkIfDataIsFilled() {
		return false;
	}

    @Override
    public boolean canChangePersonalData() {
        return true;
    }
    
}
