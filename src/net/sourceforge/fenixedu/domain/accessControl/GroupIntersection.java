package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.accessControl.IGroup;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Person;

public final class GroupIntersection extends NodeGroup {

    private static final long serialVersionUID = 1L;
    
    public GroupIntersection(IGroup ... groups) {
        super(groups);
    }

    public GroupIntersection(Collection<IGroup> groups) {
        super(groups);
    }

    @Override
    public Set<Person> getElements() {
        Set<Person> elements = super.buildSet();
        Collection<Person> elementsCollection = new ArrayList<Person>(); 
        int childrenCount = this.getChildren().size();
        if (childrenCount<2)
        {
        	throw new UnsupportedOperationException("An intersection is mathematically defined only for two or more sets");        	
        }
        else
        {
        	elementsCollection.addAll(this.getChildren().get(0).getElements());
        	for (IGroup group : this.getChildren()) {
				elementsCollection = CollectionUtils.intersection(elementsCollection,group.getElements());
			}
        }
        elements.addAll(elementsCollection);
        
        return super.freezeSet(elements);
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