package net.sourceforge.fenixedu.domain.accessControl.groups.language.operators;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupContextProvider;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.OperatorArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongNumberOfArgumentsException;

/**
 * The <code>$P</code> operator obtains the exact value of context parameter. 
 * 
 * <p>
 * If the request contains <code>"name=john+smith"</code> then
 * 
 * <code>$P(name) = "john smith"</code>
 * 
 * @author cfgi
 */
public class ParameterOperator extends OperatorArgument {

    private static final long serialVersionUID = 1L;

    public static final int NAME = 0;

    private boolean required;
    
    public ParameterOperator(Argument name) {
        super();
        
        addArgument(name);
        setRequired(false);
    }

    public ParameterOperator(String name) {
        this(new StaticArgument(name));
    }
    
    ParameterOperator(GroupContextProvider provider, Argument argument) {
        this(argument);
        
        setContextProvider(provider);
    }

    /**
     * @return the required state for the parameter argument
     */
    public boolean isRequired() {
        return this.required;
    }

    /**
     * Indicates if the parameter is required. When the parameter is required and 
     * this argument is evaluated, if the parameter is not available in the current
     * context an error is reported.
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    protected void checkOperatorArguments() {
        int size = getArguments().size();

        if (size != 1) {
            throw new WrongNumberOfArgumentsException(size, 1, 1);
        }
    }

    @Override
    protected String execute() {
        String value = getContext().getParameter(getParameterName());
        
        if (value == null && isRequired()) {
            throw new GroupDynamicExpressionException("accessControl.group.expression.operator.parameter.required", getParameterName());
        }
        else {
            return value;
        }
    }

    protected String getParameterName() {
        return String.valueOf(argument(NAME).getValue());
    }

    @Override
    protected String getMainValueString() {
        return String.format("$P(%s)", argument(NAME));
    }

}
