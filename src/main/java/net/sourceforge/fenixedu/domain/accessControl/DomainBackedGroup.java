package net.sourceforge.fenixedu.domain.accessControl;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

/**
 * @deprecated Use Bennu Groups instead
 */
@Deprecated
public abstract class DomainBackedGroup<T extends DomainObject> extends LeafGroup {

    private static final long serialVersionUID = 1L;

    private final T reference;

    public DomainBackedGroup(T object) {
        super();

        // if (object == null) {
        // throw new DomainException("accessControl.group.domainBacked.null");
        // }

        this.reference = object;
    }

    /**
     * Obtains the object that was passed in the constructor. This method uses
     * the {@link DomainReference} created to retrieve the object. As such you
     * must always consider that the target object may have been deleted and
     * thus the reference will point to <code>null</code>.
     * 
     * @return the object passed in the constructor or <code>null</code> if the
     *         object was deleted
     */
    public T getObject() {
        if (!FenixFramework.isDomainObjectValid(this.reference)) {
            throw new InvalidGroupException();
        }
        return this.reference;
    }

    @Override
    public String getExpression() {
        // HACK: when an object is deleted a domain reference starts pointing to
        // null;
        // most DomainBackedGroups, if not all, don't expect this
        if (getObject() == null) {
            return new NoOneGroup().getExpression();
        } else {
            return super.getExpression();
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this.reference == null || other == null || !this.getClass().isAssignableFrom(other.getClass())) {
            return false;
        }

        DomainBackedGroup otherGroup = (DomainBackedGroup) other;
        return this.reference.equals(otherGroup.reference);
    }

    @Override
    public int hashCode() {
        return this.reference == null ? -1 : this.reference.hashCode();
    }
}
