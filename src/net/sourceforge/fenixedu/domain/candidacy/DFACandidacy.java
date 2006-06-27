package net.sourceforge.fenixedu.domain.candidacy;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DFACandidacy extends DFACandidacy_Base {
    
    public DFACandidacy(Person person, ExecutionDegree executionDegree) {
    	super();
    	if(executionDegree == null) {
    		throw new DomainException("execution degree cannot be null");
    	}
    	if(person == null) {
    		throw new DomainException("person cannot be null");
    	}
    	if(person.getDFACandidacyByExecutionDegree(executionDegree) != null) {
    		throw new DomainException("person already has candidacy for this execution degree");
    	}
    	setExecutionDegree(executionDegree);
    	setPerson(person);
        new PreCandidacySituation(this);
    }
    
	@Override
	public Integer createCandidacyNumber() {
		//TODO criar o numero
		return null;
	}
    
}
