package net.sourceforge.fenixedu.domain.candidacy;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PaymentCandidacySituation extends PaymentCandidacySituation_Base {
    
    public  PaymentCandidacySituation(Candidacy candidacy) {
        super();
        setCandidacy(candidacy);
        Employee employee = AccessControl.getUserView().getPerson().getEmployee();
        if(employee == null) {
        	throw new DomainException("person is not an employee");
        }
    }

	@Override
	public void checkConditionsToForward() {
		// TODO: check if the candidate has already payed emulamento
		
		
	}

	@Override
	public void nextState() {
		CandidacySituation candidacySituation = new PendentCandidacySituation(getCandidacy());
	}
    
}
