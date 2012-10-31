package net.sourceforge.fenixedu.domain.cardGeneration;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class SantanderBatchSender extends SantanderBatchSender_Base {
    
    private  SantanderBatchSender() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public SantanderBatchSender(Person sender) {
	this();
	setPerson(sender);
    }
    
    public void delete() {
	removePerson();
	removeSantanderBatch();
	removeRootDomainObject();
	super.deleteDomainObject();
    }
    
}
