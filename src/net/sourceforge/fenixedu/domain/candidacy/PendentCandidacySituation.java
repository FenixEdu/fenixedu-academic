package net.sourceforge.fenixedu.domain.candidacy;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PendentCandidacySituation extends PendentCandidacySituation_Base {
    
    public  PendentCandidacySituation(Candidacy candidacy) {
        super();
        setCandidacy(candidacy);
        Employee employee = AccessControl.getUserView().getPerson().getEmployee();
        if(employee == null) {
        	throw new DomainException("person is not an employee");
        }
    }

	@Override
	public void checkConditionsToForward() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextState() {
		// TODO Auto-generated method stub
		
	}
    
}
