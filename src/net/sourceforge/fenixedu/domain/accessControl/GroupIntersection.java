package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.IGroup;

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
	resultGroups.addAll(getChildren());
	resultGroups.addAll(Arrays.asList(groups));

	for (final IGroup iter : resultGroups) {
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
    public boolean allows(IUserView userView) {
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
}