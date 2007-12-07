package net.sourceforge.fenixedu.domain.functionalities;

import java.util.UUID;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.injectionCode.IGroup;
import dml.runtime.RelationAdapter;

/**
 * An <code>AvailabilityPolicy</code> allows a functionality to indicate whom
 * may access to the funcitonality.
 * 
 * @author cfgi
 */
public abstract class AvailabilityPolicy extends AvailabilityPolicy_Base {

    static {
        ContentHasAvailabilityPolicy.addListener(new DeletePreviousAvailability());
    }
    
    protected AvailabilityPolicy() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setContentId(UUID.randomUUID().toString());
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
        removeContent();
        removeRootDomainObject();
        deleteDomainObject();
    }

    public static class DeletePreviousAvailability extends RelationAdapter<AvailabilityPolicy, Content> {
        
        @Override
        public void afterRemove(AvailabilityPolicy availabilityPolicy, Content content) {
            super.afterRemove(availabilityPolicy, content);
            
            if (content != null) {
                if (availabilityPolicy != null) {
                    availabilityPolicy.delete();
                }
            }
        }
        
    }
    
    public abstract Group getTargetGroup();
}
