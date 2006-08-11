package net.sourceforge.fenixedu.domain.accessControl.groups.language;

import java.io.Serializable;

/**
 * This class represents a property of a dynamic argument. Each property
 * can be applied to the dynamic argument's main value to obtain a sub
 * value just like regular bean properties.
 * 
 * @author cfgi
 */
public abstract class NestedProperty implements Serializable, GroupContextProvider {
    
    private GroupContextProvider provider;
    private String name;
    
    public NestedProperty(String name) {
        super();

        this.name = name;
    }

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
     * @return the name of the property
     */
    public String getName() {
        return this.name;
    }

    /**
     * Applies this nested property to the given object.
     * 
     * @param target the target object
     * 
     * @return the value of the property in the target 
     */
    public abstract Object getValue(Object target);

}
