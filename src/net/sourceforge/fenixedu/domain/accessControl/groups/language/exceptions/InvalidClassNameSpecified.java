package net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions;

/**
 * Thrown when the {@link net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.ClassOperator} cannot 
 * find a class.
 * 
 * @author cfgi
 */
public class InvalidClassNameSpecified extends GroupDynamicExpressionException {

    private static final long serialVersionUID = 1L;

    public static final String MESSAGE = "accessControl.group.expression.operator.class.invalidName";
    
    public InvalidClassNameSpecified(String prefix, String name) {
        super(MESSAGE, prefix, name);
    }

}
