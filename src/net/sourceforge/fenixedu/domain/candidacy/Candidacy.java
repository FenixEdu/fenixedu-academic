package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collections;

import org.apache.commons.digester.SetRootRule;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class Candidacy extends Candidacy_Base {
    
    protected Candidacy() {
        super();
        setOjbConcreteClass(this.getClass().getName());
        setRootDomainObject(RootDomainObject.getInstance());
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
    
    //static methods
    
    public static Candidacy readByCandidacyNumber(Integer candidacyNumber)
    {
        //TODO: FINISH
        return null;
    }
    
}
