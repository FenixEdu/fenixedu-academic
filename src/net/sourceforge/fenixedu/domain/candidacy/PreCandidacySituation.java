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
    }
	
    @Override
	public void nextState() {
    	//TODO: create payment event
		CandidacySituation candidacySituation = new PaymentCandidacySituation(getCandidacy());
	}

	@Override
	public  void checkConditionsToForward() {
		if(getCandidacy().getPerson() == null || ((DFACandidacy)getCandidacy()).getExecutionDegree() == null) {
			throw new DomainException("");
		}
	}
}
