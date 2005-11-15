package net.sourceforge.fenixedu.domain.cms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.domain.IPerson;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.iterators.IteratorChain;
import org.apache.commons.collections.iterators.UniqueFilterIterator;
public class GroupIntersection extends GroupIntersection_Base {
    	
    public GroupIntersection() {
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
		
		Iterator<IPerson> uniqueFilterIterator = new UniqueFilterIterator(iteratorChain);
		
		return uniqueFilterIterator;
		
	}
    
}