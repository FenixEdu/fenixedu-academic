package net.sourceforge.fenixedu.domain.cardGeneration;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class SantanderProblem extends SantanderProblem_Base {
    
    public  SantanderProblem() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public SantanderProblem(SantanderBatch batch, Person person, String cause) {
	this();
	setArg(person.getUsername() + "\t" + cause);
	setSantanderBatch(batch);
    }
    
    public void delete() {
	removeSantanderBatch();
	removeRootDomainObject();
	super.deleteDomainObject();
    }
    
}
