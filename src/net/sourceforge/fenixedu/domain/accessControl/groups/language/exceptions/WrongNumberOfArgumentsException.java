package net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions;

/**
 * Thrown when a wrong number of arguments were provided to an operator or builder
 * in a group expression.
 */
public class WrongNumberOfArgumentsException extends GroupDynamicExpressionException {

    private static final long serialVersionUID = 1L;

    private static final String MESSAGE1 = "accessControl.group.expression.wrong.numberOfArguments.simple";
    private static final String MESSAGE2 = "accessControl.group.expression.wrong.numberOfArguments";

    /**
     * @param given the number of arguments given
     * @param min the minimum number of arguments accepted
     * @param max the maximum number of arguments accepted
     */
    public WrongNumberOfArgumentsException(int given, int min, int max) {
        super(min < max ? MESSAGE2: MESSAGE1, String.valueOf(given), String.valueOf(min), String.valueOf(max));
    }

}
