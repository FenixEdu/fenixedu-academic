package net.sourceforge.fenixedu.domain.accessControl.groups.language;

import java.lang.reflect.Method;
import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.MethodNotAvailableException;

/**
 * The method property represents a property that takes arguments. The property
 * is still regarded as a getter but the getter can receive any number of
 * arguments.
 * 
 * @author cfgi
 */
public class MethodProperty extends NestedProperty {

    private static final long serialVersionUID = 1L;
    
    private ArgumentList arguments;

    public MethodProperty(String name) {
        super(name);

        this.arguments = new ArgumentList();
    }

    /**
     * @return the list of arguments to be passed to the method represented by
     *         this property
     */
    protected List<Argument> getArguments() {
        return this.arguments;
    }

    /**
     * @return the internal argument list
     */
    protected ArgumentList getArgumentList() {
        return this.arguments;
    }

    /**
     * Adds a new argument to the method defined by this property. Sets this
     * property as the context provider for any elements in the argument.
     * 
     * @param argument
     *            the argument to be added
     */
    public void addArgument(Argument argument) {
        argument.setContextProvider(this);

        this.arguments.add(argument);
    }

    /**
     * @inheritDoc
     * 
     * Obtains the value by considering the name of the property the name of a
     * getter. All the arguments defined for this property are used to further
     * specify the getter to be invoked.
     * 
     * @exception MethodNotAvailableException
     *                when it wasn't possible to obtain the value from the
     *                target
     */
    @Override
    public Object getValue(Object target) {
        Object[] arguments = getArgumentList().getArgumentValues();
        Class[] argumentTypes = getArgumentList().getArgumentTypes();

        try {
            Method method = target.getClass().getMethod(getMethodName(), argumentTypes);

            return method.invoke(target, arguments);
        } catch (Exception e) {
            throw new MethodNotAvailableException(e, target, getMethodName(), argumentTypes);
        }
    }

    /**
     * Obtains the final name of the method to be invoked.
     * 
     * @return the name of the method to be invoked
     */
    protected String getMethodName() {
        String name = getName();
        
        return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

}
