package net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions;

/**
 * Thrown when one of the arguments passed to a builder or operator does not have
 * to correct type.
 */
public class WrongTypeOfArgumentException extends GroupDynamicExpressionException {

    private static final long serialVersionUID = 1L;

    private static final String MESSAGE = "accessControl.group.expression.wrong.argumentType";

    /**
     * @param given the number of arguments given
     * @param min the minimum number of arguments accepted
     * @param max the maximum number of arguments accepted
     */
    public WrongTypeOfArgumentException(int position, Class expected, Class given) {
        super(MESSAGE, String.valueOf(position), expected.getName(), given.getName());
    }

}
