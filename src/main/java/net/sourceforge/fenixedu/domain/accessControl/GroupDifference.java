package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.IGroup;

import org.apache.commons.collections.CollectionUtils;

/**
 * @deprecated Use Bennu Groups instead
 */
@Deprecated
public final class GroupDifference extends NodeGroup {

    private static final long serialVersionUID = 1L;

    private final IGroup includeGroup;
    private final IGroup excludeGroup;

    public GroupDifference(IGroup includeGroup, IGroup excludeGroup) {
        super(includeGroup, excludeGroup);

        this.includeGroup = includeGroup;
        this.excludeGroup = excludeGroup;
    }

    public GroupDifference(Collection<IGroup> includeGroups, Collection<IGroup> excludeGroups) {
        super(new GroupUnion(includeGroups), new GroupUnion(excludeGroups));

        this.includeGroup = new GroupUnion(includeGroups);
        this.excludeGroup = new GroupUnion(excludeGroups);
    }

    @Override
    public Group with(final Group... group) {
        Group result = this;

        if (group != null && getIncludeGroup() instanceof Group && getExcludeGroup() instanceof Group) {
            final Group include = (Group) getIncludeGroup();
            final Group exclude = (Group) getExcludeGroup();

            for (final Group iter : group) {
                result = new GroupDifference(include.with(new GroupUnion(), iter), exclude.without(iter));
            }
        }

        return result;
    }

    @Override
    public Group without(final Group group) {
        Group result = this;

        if (group != null && getIncludeGroup() instanceof Group && getExcludeGroup() instanceof Group) {
            final Group include = (Group) getIncludeGroup();
            final Group exclude = (Group) getExcludeGroup();
            result = new GroupDifference(include.without(group), new GroupUnion(exclude, group));
        }

        return result;
    }

    protected IGroup getExcludeGroup() {
        return this.excludeGroup;
    }

    protected IGroup getIncludeGroup() {
        return this.includeGroup;
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = new HashSet<Person>();
        elements.addAll(CollectionUtils.subtract(this.includeGroup.getElements(), this.excludeGroup.getElements()));

        return super.freezeSet(elements);
    }

    @Override
    public boolean isMember(Person person) {
        return getIncludeGroup().isMember(person) && !getExcludeGroup().isMember(person);
    }

    @Override
    protected String getExpressionOperator() {
        return "-";
    }

    @Override
    public org.fenixedu.bennu.core.domain.groups.Group convert() {
        return getIncludeGroup().convert().minus(getExcludeGroup().convert());
    }
}
