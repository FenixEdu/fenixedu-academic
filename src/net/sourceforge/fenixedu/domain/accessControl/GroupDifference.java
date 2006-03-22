package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.accessControl.IGroup;
import net.sourceforge.fenixedu.domain.Person;

public final class GroupDifference extends NodeGroup {
    
    private static final long serialVersionUID = 1L;

    private class DifferenceIterator implements Iterator<Person> {
        private Iterator<Person> iterator;
        
        DifferenceIterator() {
            List<Person> persons = new ArrayList<Person>();

            Iterator<Person> includePersonsIterator = getIncludeGroup().getElementsIterator();
            while (includePersonsIterator.hasNext()) {
                Person person = includePersonsIterator.next();
                
                if (! getExcludeGroup().isMember(person)) {
                    persons.add(person);
                }
            }

            this.iterator = persons.iterator();
        }
        
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        public Person next() {
            return this.iterator.next();
        }

        public void remove() {
            throw new UnsupportedOperationException("Groups are immutable");
        }
    }

    private Group includeGroup;
    private Group excludeGroup;
    
	public GroupDifference(Collection<IGroup> includeGroups, Collection<IGroup> excludeGroups) {
        super();
        
        this.includeGroup = new GroupUnion(includeGroups);
        this.excludeGroup = new GroupUnion(excludeGroups);
    }

    protected Group getExcludeGroup() {
        return this.excludeGroup;
    }

    protected Group getIncludeGroup() {
        return this.includeGroup;
    }

	@Override
	public Iterator<Person> getElementsIterator() {
		return new DifferenceIterator();
	}

    @Override
    public boolean isMember(Person person) {
        return getIncludeGroup().isMember(person) && !getExcludeGroup().isMember(person);
    }
}
