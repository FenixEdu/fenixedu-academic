package net.sourceforge.fenixedu.domain.functionalities;

import net.sourceforge.fenixedu.domain.AccessibleItem;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import dml.runtime.RelationAdapter;

/**
 * An <code>AvailabilityPolicy</code> allows a functionality to indicate whom
 * may access to the funcitonality.
 * 
 * @author cfgi
 */
public abstract class AvailabilityPolicy extends AvailabilityPolicy_Base {

    static {
        AccessibleItemHasAvailabilityPolicy.addListener(new DeletePreviousAvailability());
    }
    
    protected AvailabilityPolicy() {
        super();

        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(this.getClass().getName());
    }

    /**
     * Determines if this policy allows the functionality to be available in the
     * given context.
     * 
     * @param context
     *            the context of the functionality
     * 
     * @return <code>true</code> if the functionality is available
     * 
     * @see Functionality#isAvailable(FunctionalityContext)
     */
    public abstract boolean isAvailable(FunctionalityContext context);
    
    /**
     * Deletes this object from persistent storage.
     */
    public void delete() {
        removeAccessibleItem();
        deleteDomainObject();
    }

    public static class DeletePreviousAvailability extends RelationAdapter<AvailabilityPolicy, AccessibleItem> {

        @Override
        public void afterRemove(AvailabilityPolicy availabilityPolicy, AccessibleItem accessibleItem) {
            super.afterRemove(availabilityPolicy, accessibleItem);

            if (accessibleItem != null) {
                if (availabilityPolicy != null) {
                    availabilityPolicy.delete();
                }
            }
        }
        
    }
}
