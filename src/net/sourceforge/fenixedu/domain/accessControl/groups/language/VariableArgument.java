package net.sourceforge.fenixedu.domain.accessControl.groups.language;

/**
 * This class represents a dynamic argument that has it's value defined by a
 * context variable.
 * 
 * @author cfgi
 */
public class VariableArgument extends DynamicArgument {

    private static final long serialVersionUID = 1L;

    private String variable;

    public VariableArgument(String variable) {
        this.variable = variable;
    }

    /**
     * @return the variable name that defines this argument's value
     */
    public String getVariable() {
        return this.variable;
    }

    /**
     * @return the value of the variable in the current context
     * 
     * @see #getVariable()
     * @see Argument#getContext()
     * 
     * @exception
     * net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.VariableNotDefinedException
     * if the variable is not defined in the current context
     */
    @Override
    public Object getMainValue() {
        return getContext().getVariable(getVariable());
    }

    /**
     * To determine the value of a variable we always required a context.
     * 
     * @return <code>true</code>
     */
    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    protected String getMainValueString() {
        return "$" + getVariable();
    }
    
}
