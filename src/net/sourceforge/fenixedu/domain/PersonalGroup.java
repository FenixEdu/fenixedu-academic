package net.sourceforge.fenixedu.domain;

import java.util.Iterator;

import net.sourceforge.fenixedu.accessControl.IGroup;
import net.sourceforge.fenixedu.applicationTier.IUserView;

public class PersonalGroup extends PersonalGroup_Base implements IGroup{
    
    public PersonalGroup() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        removePerson();

        super.deleteDomainObject();
    }
	
	public int getElementsCount()
	{
		return this.getGroup().getElementsCount();
	}

	public boolean isMember(Person person)
	{
		return this.getGroup().isMember(person);
	}

	public boolean allows(IUserView userView)
	{
		return this.getGroup().allows(userView);
	}

	public Iterator<Person> getElementsIterator()
	{
		return this.getGroup().getElementsIterator();
	}
 
}
