package net.sourceforge.fenixedu.domain.cardGeneration;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class SantanderEntry extends SantanderEntry_Base {
    
    public  SantanderEntry() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setCreated(new DateTime());
    }
    
    public SantanderEntry(SantanderBatch batch, Person person, String line) {
	this();
	setSantanderBatch(batch);
	setPerson(person);
	setLine(line);
    }
    
    public void delete() {
	removePerson();
	removeSantanderBatch();
	removeRootDomainObject();
	super.deleteDomainObject();
    }
    
}
