package net.sourceforge.fenixedu.domain;

import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class PersonalGroup extends PersonalGroup_Base implements IGroup{
    
    public PersonalGroup() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        removePerson();

        removeRootDomainObject();
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

	public Set<Person> getElements()
	{
		return this.getGroup().getElements();
	}

    @Deprecated
    public Group getGroup() {
        return super.getConcreteGroup();
    }

    @Deprecated
    public void setGroup(Group group) {
        super.setConcreteGroup(group);
    }
 
}
