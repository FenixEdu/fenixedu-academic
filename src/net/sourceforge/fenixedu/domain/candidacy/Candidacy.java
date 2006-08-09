package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;

public abstract class Candidacy extends Candidacy_Base {
    
    protected Candidacy() {
        super();
        setOjbConcreteClass(this.getClass().getName());
        setNumber(createCandidacyNumber());
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public Candidacy(CandidacySituation candidacySituation) {
    	this();
    	if(candidacySituation == null) {
    		throw new DomainException("candidacy situation cannot be null");
    	}
    	this.addCandidacySituations(candidacySituation);
    }
    
    public final Integer createCandidacyNumber() {
        if(RootDomainObject.getInstance().getCandidaciesCount() == 0){
            return Integer.valueOf(1);
        }
        Candidacy candidacy = (Candidacy) Collections.max(RootDomainObject.getInstance().getCandidaciesSet(), new BeanComparator("number"));
        return candidacy.getNumber() + 1; 
    }
    
    public CandidacySituation getActiveCandidacySituation(){
        return Collections.max(getCandidacySituations(), CandidacySituation.DATE_COMPARATOR);
    }
    
    //static methods
    
    public static Candidacy readByCandidacyNumber(Integer candidacyNumber) {
        for (Candidacy candidacy : RootDomainObject.getInstance().getCandidacies()) {
            if(candidacy.getNumber().equals(candidacyNumber)){
                return candidacy;
            }
        }
        return null;
    }
    
    public abstract String getDescription();
}
