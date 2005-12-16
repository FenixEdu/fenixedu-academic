package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.IPerson;
import relations.RoleUserGroupHook;
public class RoleGroup extends RoleGroup_Base {
    
    public RoleGroup() {
        super();
    }

    @Override
	public int getElementsCount()
	{
    	return this.getRole().getAssociatedPersonsCount();
	}

	@Override
	public Iterator<IPerson> getElementsIterator()
	{
		return this.getRole().getAssociatedPersons().iterator();		
	}
	
	@Override
    public void delete()
    {
    	RoleUserGroupHook.remove(this.getRole(),this);
    	super.delete();
    }
    
}
