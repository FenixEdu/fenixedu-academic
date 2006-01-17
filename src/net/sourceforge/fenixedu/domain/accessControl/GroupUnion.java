package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.domain.Person;

import org.apache.commons.collections.iterators.IteratorChain;

public final class GroupUnion extends NodeGroup {

    private static final long serialVersionUID = 1L;

    public GroupUnion(Group ... groups) {
        super(groups);
    }

    public GroupUnion(Collection<Group> groups) {
        super(groups);
    }

    @Override
    public Iterator<Person> getElementsIterator() {
        IteratorChain iteratorChain = new IteratorChain();

        for (Group part : this.getChildren()) {
            iteratorChain.addIterator(part.getElementsIterator());
        }

        return iteratorChain;
    }

    @Override
    public boolean isMember(Person person) {
        return super.isMember(person);
    }
}
