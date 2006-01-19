package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;

public abstract class DomainBackedGroup<T extends DomainObject> extends LeafGroup {
    
    private static final long serialVersionUID = 1L;
    
    private DomainReference<T> reference;
    
    public DomainBackedGroup(T object) {
        super();
        
        this.reference = new DomainReference<T>(object);
    }

    @Override
    public Iterator<Person> getElementsIterator() {
        return null;
    }

    public T getObject() {
        return this.reference.getObject();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        
        if (! this.getClass().isAssignableFrom(other.getClass())) {
            return false;
        }
        
        DomainBackedGroup otherGroup = (DomainBackedGroup) other;
        return this.reference.equals(otherGroup.reference);        
    }

    @Override
    public int hashCode() {
        return this.reference.hashCode();
    }
}
