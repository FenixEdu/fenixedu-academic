package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class DomainBackedGroup<T extends DomainObject> extends LeafGroup {
    
    private static final long serialVersionUID = 1L;
    
    private DomainReference<T> reference;
    
    public DomainBackedGroup(T object) {
        super();
     
        if (object == null) {
            throw new DomainException("accessControl.group.domainBacked.null");
        }
        
        this.reference = new DomainReference<T>(object);
    }

    /**
     * Obtains the object that was passed in the constructor. This method uses
     * the {@link DomainReference} created to retrieve the object. As such you
     * must always consider that the target object may have been deleted and
     * thus the reference will point to <code>null</code>.
     * 
     * @return the object passed in the constructor or <code>null</code> if
     *         the object was deleted
     */
    public T getObject() {
        return this.reference.getObject();
    }
    
    @Override
    public String getExpression() {
        // HACK: when an object is deleted a domain reference starts pointing to null;
        // most DomainBackedGroups, if not all, don't expect this
        if (getObject() == null) {
            return new NoOneGroup().getExpression();
        }
        else {
            return super.getExpression();
        }
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
