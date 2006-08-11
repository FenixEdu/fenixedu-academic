package net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions;

/**
 * This exception is thrown from a {@link net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupContext group context}
 * when a requested variable is not defined in the context.
 * 
 * @author cfgi
 */
public class VariableNotDefinedException extends GroupDynamicExpressionException {

    private static final long serialVersionUID = 1L;
    
    private static final String MESSAGE = "accessControl.group.expression.variable.notDefined";
    
    public VariableNotDefinedException(String variable) {
        super(MESSAGE, variable);
    }

}
