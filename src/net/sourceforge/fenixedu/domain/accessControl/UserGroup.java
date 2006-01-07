/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * tags
 * Created on 23:13:44,20/Set/2005
 * @version $Id$
 */


package net.sourceforge.fenixedu.domain.accessControl;


import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import relations.GroupCreation;
import relations.GroupMailingList;
import relations.GroupOwnership;

public abstract class UserGroup extends UserGroup_Base
{

	public UserGroup()
	{
		super();
	}

	public abstract java.util.Iterator<net.sourceforge.fenixedu.domain.Person> getElementsIterator();

	/**
	 * Provides a standard implementation to <code>count()</code><br/>
	 * It accesses the elements iterator and counts how many sucessfull <code>next()<code> can be called on it<br/>
	 * If any group subclassing this class can provide a more efficient way of calculating its size, then override this method
	 */
	public int getElementsCount()
	{
		int elementsCount = 0;
		Iterator iterator = this.getElementsIterator();
		while (iterator.hasNext())
		{
			elementsCount++;
			iterator.next();
		}

		return elementsCount;
	}
	
	public boolean isMember(Person person)
	{
		boolean result = false;
		Iterator<Person> persons = this.getElementsIterator();
		while(persons.hasNext())
		{
			if (person.equals(persons.next()))
			{
				result=true;
				break;
			}
		}
		
		return result;
	}	
	
	public void delete()
	{
		GroupMailingList.remove(this.getMailingList(),this);
		if (this.getMailingList() != null && this.getMailingList().getGroups().size()==0)
			this.getMailingList().delete();		
		for (Person user : this.getOwners())
		{
			GroupOwnership.remove(user,this);
		}
		
		GroupCreation.remove(this.getCreator(),this);
		
		this.deleteDomainObject();
	}
	
	public boolean allows(IUserView userView)
	{
		boolean result = false;
		Person person = userView.getPerson();
		if (person!=null)
		{
			result = this.isMember(person);
		}
				
		return result;
	}

}
