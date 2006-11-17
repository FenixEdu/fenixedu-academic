package net.sourceforge.fenixedu.domain.accessControl.groups.language;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An argument that represents a dynamic value. 
 * 
 * @author cfgi
 */
public abstract class DynamicArgument extends Argument {

    private List<NestedProperty> properties;
    
    public DynamicArgument() {
        super();
        
        this.properties = new ArrayList<NestedProperty>();
    }

    /**
     * Adds a new nested property to this dynamic argument and
     * makes this argument the context provider for the property.
     * 
     * @param property the property to add
     */
    public void addProperty(NestedProperty property) {
        property.setContextProvider(this);
        this.properties.add(property);
    }
    
    /**
     * Obtains the list of properties that are applied to the main value of 
     * this argument. 
     * 
     * @return the nested properties that were defined for this argument
     */
    public List<NestedProperty> getProperties() {
        return this.properties;
    }

    /**
     * Obtains the value of this argument by obtaining the main value of the
     * argument and them by applying all the properties of this argument to
     * a sequence of values starting from the main value.
     * 
     * @return the value of this argument
     */
    @Override
    public Object getValue() {
        Object value = getMainValue();
        
        for (NestedProperty property : getProperties()) {
            value = property.getValue(value);
        }
        
        return value;
    }

    /**
     * Obtains the main value of the argument. The main value is the value
     * that makes this argument dynamic.
     * 
     * @return the main value for this dynamic argument
     */
    protected abstract Object getMainValue();
    
    /**
     * A dynamic value is by definition dynamic.
     * 
     * @return <code>true</code>
     */
    @Override
    public boolean isDynamic() {
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append(getMainValueString());
        
        Iterator<NestedProperty> iterator = getProperties().iterator();
        while (iterator.hasNext()) {
            builder.append("." + iterator.next().toString());
        }
        
        return builder.toString();
    }

    /**
     * @return the string representation of the main value part to compose the
     *         final toString() representation
     */
    protected abstract String getMainValueString();
}
