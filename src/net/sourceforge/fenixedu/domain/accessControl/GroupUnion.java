

package net.sourceforge.fenixedu.domain.accessControl;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.collections.iterators.IteratorChain;
import org.apache.commons.collections.iterators.UniqueFilterIterator;

import net.sourceforge.fenixedu.domain.IPerson;

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
