package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;

public final class GroupIntersection extends NodeGroup {

    private static final long serialVersionUID = 1L;

    private class IntersectionIterator implements Iterator<Person> {
        private Iterator<Person> iterator;
        
        IntersectionIterator() {
            List<Person> persons = new ArrayList<Person>();
            
            if (! getChildren().isEmpty()) {
                Iterator<Person> iterator = getChildren().get(0).getElementsIterator();
                
                outter: 
                while (iterator.hasNext()) {
                    Person person = iterator.next();
                    
                    for (Group group : getChildren()) {
                        if (! group.isMember(person)) {
                            continue outter;
                        }
                    }
                    
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
    
    public GroupIntersection(Group ... groups) {
        super(groups);
    }

    public GroupIntersection(Collection<Group> groups) {
        super(groups);
    }

    @Override
    public Iterator<Person> getElementsIterator() {
        return new IntersectionIterator();
    }

}