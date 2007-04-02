package net.sourceforge.fenixedu.domain.accessControl.groups.language;

import java.util.List;

/**
 * An operator represents a dynamic argument that obtains it's value by using a
 * specific operation available to the group expression. The operation can
 * receive arguments and thus be parameterizable.
 * 
 * @author cfgi
 */
public abstract class OperatorArgument extends DynamicArgument {

    private ArgumentList arguments;

    public OperatorArgument() {
        super();

        this.arguments = new ArgumentList();
    }

    /**
     * @return the list of arguments available to this dynamic group
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
     * Adds a new argument to the operator. Sets this argument as the context
     * provider for any elements in the argument.
     * 
     * @param argument
     *            the argument to be added
     */
    public void addArgument(Argument argument) {
        argument.setContextProvider(this);

        this.arguments.add(argument);
    }

    @Override
    protected Object getMainValue() {
        checkOperatorArguments();

        return execute();
    }

    /**
     * Checks if the operator has all the required arguments.
     * 
     * @exception WrongNumberOfArgumentsException
     *                if the operator does not have all the required arguments
     * @exception InvalidArgumentException
     *                if one of the arguments is not valid
     */
    protected void checkOperatorArguments() {
        // do nothing
    }

    /**
     * Executes the operator.
     * 
     * @return the value of the execution
     */
    protected abstract Object execute();

    /**
     * Auxiliary method to make getting a specific argument more readable.
     * 
     * @param index
     *            the number of the argument
     * @return the requested argument
     */
    protected Argument argument(int index) {
        return getArgumentList().get(index);
    }

    @Override
    public boolean isDynamic() {
        for (Argument argument : getArguments()) {
            if (argument.isDynamic()) {
                return true;
            }
        }
        
        return false;
    }
    
}
