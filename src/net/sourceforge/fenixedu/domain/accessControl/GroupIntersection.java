package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.Person;

import org.apache.commons.collections.iterators.IteratorChain;
import org.apache.commons.collections.iterators.UniqueFilterIterator;
public class GroupIntersection extends GroupIntersection_Base {
    	
    public GroupIntersection() {
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
		
		Iterator<Person> uniqueFilterIterator = new UniqueFilterIterator(iteratorChain);
		
		return uniqueFilterIterator;
		
	}
    
}