package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.IGroup;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.NobodyGroup;

/**
 * @deprecated Use Bennu Groups instead
 */
@Deprecated
public final class GroupUnion extends NodeGroup {

    private static final long serialVersionUID = 1L;

    public GroupUnion(IGroup... groups) {
        super(groups);
    }

    public GroupUnion(Collection<IGroup> groups) {
        super(groups);
    }

    @Override
    public Group with(final Group... groups) {
        final List<IGroup> resultGroups = new ArrayList<IGroup>();

        for (final IGroup iter : getChildren()) {
            if (iter instanceof GroupUnion) {
                resultGroups.addAll(((GroupUnion) iter).getChildren());
            } else {
                resultGroups.add(iter);
            }
        }

        for (final IGroup iter : Arrays.asList(groups)) {
            if (iter instanceof GroupUnion) {
                resultGroups.addAll(((GroupUnion) iter).getChildren());
            } else {
                resultGroups.add(iter);
            }
        }

        return new GroupUnion(resultGroups);
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
            result = updated.size() == 1 ? (Group) updated.iterator().next() : new GroupUnion(updated);
        }

        return result;
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
    public boolean allows(User userView) {
        for (IGroup group : getChildren()) {
            if (group.allows(userView)) {
                return true;
            }
        }
        return false;
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

    @Override
    protected String getExpressionOperator() {
        return "||";
    }

    @Override
    public org.fenixedu.bennu.core.domain.groups.Group convert() {
        org.fenixedu.bennu.core.domain.groups.Group group = NobodyGroup.getInstance();
        for (IGroup child : getChildren()) {
            group = group.or(child.convert());
        }
        return group;
    }
}
