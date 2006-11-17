package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public final class GroupDifference extends NodeGroup {
    
    private static final long serialVersionUID = 1L;
       
    private IGroup includeGroup;
    private IGroup excludeGroup;
    
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

}
