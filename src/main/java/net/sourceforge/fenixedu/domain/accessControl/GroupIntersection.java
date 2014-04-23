package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.IGroup;

import org.apache.commons.collections.CollectionUtils;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.AnyoneGroup;

/**
 * @deprecated Use Bennu Groups instead
 */
@Deprecated
public final class GroupIntersection extends NodeGroup {

    private static final long serialVersionUID = 1L;

    public GroupIntersection(IGroup... groups) {
        super(groups);
    }

    public GroupIntersection(Collection<IGroup> groups) {
        super(groups);
    }

    @Override
    public Group with(final Group... groups) {
        final List<IGroup> resultGroups = new ArrayList<IGroup>();

        for (final IGroup iter : getChildren()) {
            if (iter instanceof GroupIntersection) {
                resultGroups.addAll(((GroupIntersection) iter).getChildren());
            } else {
                resultGroups.add(iter);
            }
        }

        for (final IGroup iter : Arrays.asList(groups)) {
            if (iter instanceof GroupIntersection) {
                resultGroups.addAll(((GroupIntersection) iter).getChildren());
            } else {
                resultGroups.add(iter);
            }
        }

        return new GroupIntersection(resultGroups);
    }

    @Override
    public Group without(final Group group) {
        Group result = this;

        if (group != null) {
            final List<IGroup> updated = new ArrayList<IGroup>();
            for (final IGroup iter : getChildren()) {
                if (!iter.equals(group) && iter instanceof Group) {
                    updated.add(((Group) iter).without(group));
                }
            }
            result = updated.size() == 1 ? (Group) updated.iterator().next() : new GroupIntersection(updated);
        }

        return result;
    }

    @Override
    public Set<Person> getElements() {
        Collection<Person> elementsCollection = null;

        for (Object element : getChildren()) {
            IGroup group = (IGroup) element;

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
    public boolean allows(User userView) {
        for (IGroup group : getChildren()) {
            if (!group.allows(userView)) {
                return false;
            }
        }
        return !getChildren().isEmpty();
    }

    @Override
    public boolean isMember(Person person) {
        for (IGroup group : getChildren()) {
            if (!group.isMember(person)) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected String getExpressionOperator() {
        return "&&";
    }

    @Override
    public org.fenixedu.bennu.core.domain.groups.Group convert() {
        org.fenixedu.bennu.core.domain.groups.Group group = AnyoneGroup.getInstance();
        for (IGroup child : getChildren()) {
            group = group.and(child.convert());
        }
        return group;
    }
}