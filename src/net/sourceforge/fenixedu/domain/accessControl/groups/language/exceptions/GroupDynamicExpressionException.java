package net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions;

/**
 * This exception is throw by dynamic elements of an expression to signal a problem
 * in the evaluation of an expression.
 * 
 * @author cfgi
 */
public class GroupDynamicExpressionException extends GroupExpressionException {

    private static final long serialVersionUID = 1L;

    public GroupDynamicExpressionException(String key, String... args) {
        super(key, args);
    }

    public GroupDynamicExpressionException(Throwable cause, String key, String... args) {
        super(cause, key, args);
    }

}
