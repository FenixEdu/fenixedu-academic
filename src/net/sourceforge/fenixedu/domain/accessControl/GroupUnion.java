

package net.sourceforge.fenixedu.domain.accessControl;


import java.util.Iterator;

import net.sourceforge.fenixedu.domain.Person;

import org.apache.commons.collections.iterators.IteratorChain;

public class GroupUnion extends GroupUnion_Base
{

	public GroupUnion()
	{
		super();
	}
	
	@Override
	public Iterator<Person> getElementsIterator()
	{
		IteratorChain iteratorChain = new IteratorChain();
		
		for (UserGroup part : this.getParts())
		{	
			iteratorChain.addIterator(part.getElementsIterator());
		}
		
		return iteratorChain;
		
	}

}
