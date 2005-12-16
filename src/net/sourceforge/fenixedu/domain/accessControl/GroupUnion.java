

package net.sourceforge.fenixedu.domain.accessControl;


import java.util.Iterator;

import net.sourceforge.fenixedu.domain.IPerson;

import org.apache.commons.collections.iterators.IteratorChain;

public class GroupUnion extends GroupUnion_Base
{

	public GroupUnion()
	{
		super();
	}
	
	@Override
	public Iterator<IPerson> getElementsIterator()
	{
		IteratorChain iteratorChain = new IteratorChain();
		
		for (IUserGroup part : this.getParts())
		{	
			iteratorChain.addIterator(part.getElementsIterator());
		}
		
		return iteratorChain;
		
	}

}
