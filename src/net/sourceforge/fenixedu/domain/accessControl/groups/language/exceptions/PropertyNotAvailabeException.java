package net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions;

/**
 * Thrown when a nested property is applied to the target and the target
 * does not support the property. 
 * 
 * @author cfgi
 */
public class PropertyNotAvailabeException extends GroupDynamicExpressionException {

    private static final long serialVersionUID = 1L;
    
    private static final String MESSAGE = "accessControl.group.expression.property.notAvailable";
    
    private Object target;
    private String name;
    
    protected PropertyNotAvailabeException(Throwable cause, Object target, String name, String key, String ... args) {
        super(key, args);
        
        this.target = target;
        this.name = name;
    }
    
    public PropertyNotAvailabeException(Throwable cause, Object target, String name) {
        this(cause, target, name, MESSAGE, target.getClass().getName(), name);
    }
    
    /**
     * @return the name of the property
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the target object of the property 
     */
    public Object getTarget() {
        return this.target;
    }
    
}
