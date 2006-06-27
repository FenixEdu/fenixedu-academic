package net.sourceforge.fenixedu.domain.candidacy;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PreCandidacySituation extends PreCandidacySituation_Base {
    
	public  PreCandidacySituation(Candidacy candidacy) {
        super();
        setCandidacy(candidacy);
        Employee employee = AccessControl.getUserView().getPerson().getEmployee();
        if(employee == null) {
        	throw new DomainException("person is not an employee");
        }
        setEmployee(employee);
        //TODO: create payment event
    }
	
    @Override
	public void nextState() {
		CandidacySituation candidacySituation = new StandByCandidacySituation(getCandidacy());
	}

	@Override
	public  void checkConditionsToForward() {
    	//TODO: check if the candidate has already payed emulamento
	}
}
