package net.sourceforge.fenixedu.domain.accessControl.groups.language.operators;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupContextProvider;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.OperatorArgument;
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
    
    public ParameterOperator(Argument name) {
        super();
        
        addArgument(name);
    }

    ParameterOperator(GroupContextProvider provider, Argument argument) {
        this(argument);
        
        setContextProvider(provider);
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
        return getContext().getParameter(getParameterName());
    }

    protected String getParameterName() {
        return String.valueOf(getArguments().get(NAME).getValue());
    }

}
