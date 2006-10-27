package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.FieldIsRequiredException;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.log4j.Logger;

public class AccessibleItem extends AccessibleItem_Base {
    
    private static final Logger logger = Logger.getLogger(AccessibleItem.class);
    
    public  AccessibleItem() {
        super();
        
        setOjbConcreteClass(this.getClass().getName());
        setEnabled(true);
        setVisible(true);
    }

    /**
     * Changes the visible internationalizable name of the item. The name must
     * exist, that is, it may not be <code>null</code> or an empty
     * multilanguage string.
     * 
     * @see MultiLanguageString#isEmpty()
     */
    @Override
    public void setName(MultiLanguageString name) {
        if (name == null || name.isEmpty()) {
            throw new FieldIsRequiredException("name", "functionalities.accessible.item.required.name");
        }

        super.setName(name);
    }

    @Override
    public void setTitle(MultiLanguageString title) {
        if (title == null || title.isEmpty()) {
            super.setTitle(null);
        } else {
            super.setTitle(title);
        }
    }

    /**
     * @see #isEnabled()
     */
    @Override
    @Deprecated
    public Boolean getEnabled() {
        return super.getEnabled();
    }

    /**
     * Indicates if a functionality is generally available for use. A disabled
     * functionality is never available despite what the current availability
     * policy is. When a functionality is enabled then it is availability is
     * defined by the current availability policy.
     * 
     * @return <code>true</code> if this functionality is available for
     *         general use
     */
    public Boolean isEnabled() {
        return super.getEnabled();
    }

    /**
     * Checks if this functionality is visible for the given person and context.
     * This method may be used to decide if a certain functionality is displayed
     * in the interface or not.
     * 
     * @param context
     *            the current context
     * @param person
     *            the accessing the functionality or <code>null</code> if it's
     *            the public requester
     * 
     * @return <code>true</code> if the functionality should be displayed to
     *         the user
     */
    public boolean isVisible(FunctionalityContext context) {
        return isVisible() && isAvailable(context);
    }

    /**
     * @return <code>true</code> if this item was marked as visible
     *         to the user
     */
    public Boolean isVisible() {
        return getVisible() == null ? true : getVisible();
    }

    /**
     * Checks is this item is available in the given context, that is, if the
     * person hold in the context may click the item to start an activity or
     * usecase.
     * 
     * @return <code>true</code> if the item is available for the
     *         person hold in the context
     */
    public boolean isAvailable(FunctionalityContext context) {
        if (context == null) {
            return true;
        }

        if (!isEnabled()) {
            return false;
        }

        if (getAvailabilityPolicy() == null) {
            return true;
        }

        try {
            return getAvailabilityPolicy().isAvailable(context);
        }
        catch (Exception e) {
            logger.warn("an error occured while checking the availability of " + this);
            e.printStackTrace();
            
            return false;
        }
    }
    
    /**
     * Deletes this object accessible item.
     * 
     * <p>
     * This delete method is a template method for all accessible items. First
     * {@link #checkDeletion()} is called. If the object is not deletable then a
     * subclass must throw a {@link DomainException} explaining why. If no
     * exception is thrown then {@link #disconnect()} is called to allow the
     * object to remove any specific relations. After that {@link #deleteSelf()}
     * is called to allow object finalization.
     * 
     * <p>
     * After all this the standard relations of a functionality are removed and
     * the object is marked for deletion in the database.
     * 
     * @throws DomainException
     *             if the item cannot be deleted
     */
    public void delete() {
        checkDeletion();
        disconnect();
        deleteSelf();
        deleteDomainObject();
    }
    
    /**
     * Verifies if this item can bve deleted.
     * 
     * @return <code>true</code> by default
     */
    public boolean isDeletable() {
        return true;
    }

    /**
     * Checks if this item can be deleted and throws and exception if it can't.
     * 
     * @throws DomainException
     *             if this item cannot be deleted
     */
    protected void checkDeletion() {
        if (! isDeletable()) {
            throw new DomainException("accessibleItem.delete.notAvailable");
        }
    }

    /**
     * Removes any specific relations the item has. Subclasses that override
     * this method <strong>must</strong> call super to remove all relations.
     * 
     * <p>
     * If other objects should be deleted because of this object beeing deleted,
     * this is the place to do it.
     */
    protected void disconnect() {
        if (hasAvailabilityPolicy()) {
            getAvailabilityPolicy().delete();
        }
    }
    
    /**
     * Finalizes the state of this item, that is, does the last finalization
     * after beeing disconnected but before being marked for deleting from the
     * persistent storage.
     */
    protected void deleteSelf() {
        // do nothing
    }
}
