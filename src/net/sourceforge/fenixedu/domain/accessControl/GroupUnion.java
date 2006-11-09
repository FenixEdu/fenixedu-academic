package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public final class GroupUnion extends NodeGroup {

    private static final long serialVersionUID = 1L;

    public GroupUnion(IGroup... groups) {
        super(groups);
    }

    public GroupUnion(Collection<IGroup> groups) {
        super(groups);
    }

    @Override
    public int getElementsCount() {
        int count = 0;
        
        for (IGroup child : getChildren()) {
            count += child.getElementsCount();
        }
        
        return count;
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = new HashSet<Person>();

        for (IGroup child : getChildren()) {
            elements.addAll(child.getElements());
        }

        return elements;

    }

    @Override
    public boolean isMember(Person person) {
        for (IGroup group : getChildren()) {
            if (group.isMember(person)) {
                return true;
            }
        }

        return false;
    }
}
