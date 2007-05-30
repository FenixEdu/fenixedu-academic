package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;

public class PersistentGroup extends DomainBackedGroup<PersistentGroupMembers> {

    private static final long serialVersionUID = 156456365385985497L;
    
    public PersistentGroup(PersistentGroupMembers object) {
	super(object);	
    }

    public String getName() {
    	return getObject().getName();
    }
    
    @Override
    public Set<Person> getElements() {	
	PersistentGroupMembers groupMembers = getObject();
	return Collections.unmodifiableSet(groupMembers.getPersonsSet());	
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] {new IdOperator(getObject())};
    }
    
    @Override
    public boolean isMember(Person person) {
	if(person != null) {
	    PersistentGroupMembers groupMembers = getObject();
	    for (Person groupPerson : groupMembers.getPersons()) {
		if(person.equals(groupPerson)) {
		    return true;
		}
	    }
	}
	return false;
    }
    
    public static class Builder implements GroupBuilder {
	
        public Group build(Object[] arguments) {
            return new PersistentGroup((PersistentGroupMembers) arguments[0]);
        }

        public int getMinArguments() {
            return 1;
        }

        public int getMaxArguments() {
            return 1;
        }        
    }
}
