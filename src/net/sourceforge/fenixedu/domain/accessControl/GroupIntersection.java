package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.Person;

import org.apache.commons.collections.iterators.IteratorChain;
import org.apache.commons.collections.iterators.UniqueFilterIterator;

public class GroupIntersection extends NodeGroup {

    private static final long serialVersionUID = 1L;

    public GroupIntersection(Group ... groups) {
        super(groups);
    }

    @Override
    public Iterator<Person> getElementsIterator() {
        IteratorChain iteratorChain = new IteratorChain();

        for (Group part : this.getChildren()) {
            iteratorChain.addIterator(part.getElementsIterator());
        }

        Iterator<Person> uniqueFilterIterator = new UniqueFilterIterator(iteratorChain);

        return uniqueFilterIterator;

    }

}