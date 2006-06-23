package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class Candidacy extends Candidacy_Base {
    
    protected Candidacy() {
        super();
        setNumber(createCandidacyNumber());
    }
    
    public Candidacy(CandidacySituation candidacySituation) {
    	this();
    	if(candidacySituation == null) {
    		throw new DomainException("candidacy situation cannot be null");
    	}
    	this.addCandidacySituations(candidacySituation);
    }
    
    public abstract Integer createCandidacyNumber();
    
    public CandidacySituation getActiveCandidacySituation(){
        return Collections.max(getCandidacySituations(), CandidacySituation.DATE_COMPARATOR);
    }
    
}
