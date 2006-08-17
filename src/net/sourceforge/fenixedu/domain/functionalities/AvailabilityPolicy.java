package net.sourceforge.fenixedu.domain.functionalities;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * An <code>AvailabilityPolicy</code> allows a functionality to indicate whom
 * may access to the funcitonality.
 * 
 * @author cfgi
 */
public abstract class AvailabilityPolicy extends AvailabilityPolicy_Base {

    protected AvailabilityPolicy() {
        super();

        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(this.getClass().getName());
    }

    /**
     * Determines if this policy allows the given person to access to the
     * {@link #getFunctionality() referred} functionality. 
     * 
     * @param context
     *            the context of the functionality
     * @param person
     *            the person trying to access the functionality
     * 
     * @return <code>true</code> if the person may access the functionality
     * 
     * @see Functionality#isAvailable(FunctionalityContext, Person)
     */
    public abstract boolean isAvailable(FunctionalityContext context, Person person);
    
    /**
     * Deletes this object from persistent storage.
     */
    public void delete() {
        removeFunctionality();
        deleteDomainObject();
    }

}
