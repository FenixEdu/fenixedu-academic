package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import relations.PersonUserGroupHook;
public class PersonGroup extends PersonGroup_Base {
    
    public PersonGroup() {
        super();
    }

    public PersonGroup(Person creator, Person person, NodeGroup aggregator) {
        super();
        setCreator(creator);
        setPerson(person);
        addAggregators(aggregator);
    }
        
    
	@Override
	public int getElementsCount()
	{
		return 1;
	}

	@Override
	public Iterator<Person> getElementsIterator()
	{
		List<Person> singlePersonList = new ArrayList<Person>(1);
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
	public boolean isMember(net.sourceforge.fenixedu.domain.Person person)
	{
		return this.getPerson().equals(person);
	}
    
}
