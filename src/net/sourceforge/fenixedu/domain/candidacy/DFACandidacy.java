package net.sourceforge.fenixedu.domain.candidacy;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DFACandidacy extends DFACandidacy_Base {
    
    public DFACandidacy(Person person, ExecutionDegree executionDegree) {
    	if(executionDegree == null) {
    		throw new DomainException("execution degree cannot be null");
    	}
    	if(person == null) {
    		throw new DomainException("person cannot be null");
    	}
    	setExecutionDegree(executionDegree);
        addCandidacySituations(new PreCandidacySituation(this));
    }
    
	@Override
	public Integer createCandidacyNumber() {
		return null;
	}
    
}
