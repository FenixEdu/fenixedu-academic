package net.sourceforge.fenixedu.domain.accessControl.groups.language;

import java.io.Serializable;

/**
 * This class represents a possible argument for a dynamic group or
 * method property. It's reponsible for the abstraction of how the 
 * argument's value is obtained.
 * 
 * @author cfgi
 */
public abstract class Argument implements Serializable, GroupContextProvider {

    private GroupContextProvider provider;

    /**
     * Sets the context provider for this property and any dynamic
     * elements it may contain.
     * 
     * @param provider the new context provider for the property
     */
    public void setContextProvider(GroupContextProvider provider) {
        this.provider = provider;
    }
    
    /**
     * @return the current group context
     */
    public GroupContext getContext() {
        return this.provider.getContext();
    }

    /**
     * Calculates the, possibly dynamic, argument's value.
     * 
     * @return the argument's value 
     */
    public abstract Object getValue();
    
    /**
     * Gets the value's declared type. The default is to return
     * the runtime class of the argument's value but this method
     * allows to override that behaviour.
     * 
     * @return the argument's type
     */
    public Class getType() {
        Object value = getValue();
        
        if (value == null) {
            return null;
        }
        else {
            return value.getClass();
        }
    }
    
    /**
     * @return <code>true</code> if the value of this argument is context dependant
     */
    public abstract boolean isDynamic();
}
