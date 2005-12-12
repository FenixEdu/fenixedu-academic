package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IPerson;
import relations.PersonUserGroupHook;
public class PersonGroup extends PersonGroup_Base {
    
    public PersonGroup() {
        super();
    }
	    
	@Override
	public int getElementsCount()
	{
		return 1;
	}

	@Override
	public Iterator<IPerson> getElementsIterator()
	{
		List<IPerson> singlePersonList = new ArrayList<IPerson>(1);
		singlePersonList.add(this.getPerson());
		return singlePersonList.iterator();
	}
	
	@Override
    public void delete()
    {
		PersonUserGroupHook.remove(this.getPerson(),this);
    	super.delete();
    }
	
	@Override
	public boolean isMember(net.sourceforge.fenixedu.domain.IPerson person)
	{
		return this.getPerson().equals(person);
	}
    
}
