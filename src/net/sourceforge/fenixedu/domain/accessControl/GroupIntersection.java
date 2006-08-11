package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import net.sourceforge.fenixedu.accessControl.IGroup;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Person;

public final class GroupIntersection extends NodeGroup {

    private static final long serialVersionUID = 1L;

    public GroupIntersection(IGroup... groups) {
        super(groups);
    }

    public GroupIntersection(Collection<IGroup> groups) {
        super(groups);
    }

    @Override
    public Set<Person> getElements() {
        Collection<Person> elementsCollection = null;

        for (Iterator iter = getChildren().iterator(); iter.hasNext();) {
            IGroup group = (IGroup) iter.next();

            if (elementsCollection == null) {
                elementsCollection = new ArrayList<Person>(group.getElements());
            } else {
                elementsCollection = CollectionUtils.intersection(elementsCollection, group.getElements());
            }
        }

        Set<Person> elements = buildSet();
        if (elementsCollection != null) {
            elements.addAll(elementsCollection);
        }

        return freezeSet(elements);
    }

    @Override
    public boolean isMember(Person person) {
        for (IGroup group : getChildren()) {
            if (! group.isMember(person)) {
                return false;
            }
        }

        return true;
    }
}