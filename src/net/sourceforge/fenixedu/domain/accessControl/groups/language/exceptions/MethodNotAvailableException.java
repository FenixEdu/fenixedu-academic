package net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions;

import java.util.Arrays;

/**
 * Thrown when a method property cannot inoke the specified method.
 * 
 * @author cfgi
 */
public class MethodNotAvailableException extends PropertyNotAvailabeException {

    private static final long serialVersionUID = 1L;
    
    private static final String MESSAGE = "accessControl.group.expression.method.notAvailable";
    
    private Class[] argumentTypes;
    
    public MethodNotAvailableException(Throwable cause, Object target, String name, Class[] types) {
        super(cause, target, name, MESSAGE, target.getClass().getName(), name, Arrays.toString(types));
        
        this.argumentTypes = types;
    }

    /**
     * @return the types of the arguments used to lookup the method
     */
    public Class[] getArgumentTypes() {
        return this.argumentTypes;
    }

}
