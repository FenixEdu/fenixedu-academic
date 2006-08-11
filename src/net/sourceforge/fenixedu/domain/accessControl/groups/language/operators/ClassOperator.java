package net.sourceforge.fenixedu.domain.accessControl.groups.language.operators;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupContextProvider;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.OperatorArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.InvalidClassNameSpecified;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongNumberOfArgumentsException;

/**
 * The <code>$C</code> operator translates its argument into a class. The
 * class is specified by name. This operator is specialized for domain classes
 * so you can ommit the commom prefix of any domain class:
 * <code>net.sourceforge.fenixedu.domain.<code>.
 * 
 * <p>
 * <code>$C('Person') = class&nbsp;net.sourceforge.fenixedu.domain.Person<code>
 * <code>$C('accounting.Event') = class&nbsp;net.sourceforge.fenixedu.domain.accounting.Event<code>
 * <code>$C('java.lang.Object') = class&nbsp;java.lang.Object<code>
 * 
 * @author cfgi
 */
public class ClassOperator extends OperatorArgument {

    private static final long serialVersionUID = 1L;

    public static final int CLASS_NAME = 0;

    public static final String DOMAIN_PREFIX = "net.sourceforge.fenixedu.domain.";

    public ClassOperator(Argument argument) {
        super();

        addArgument(argument);
    }

    ClassOperator(GroupContextProvider provider, Argument argument) {
        super();

        setContextProvider(provider);
        addArgument(argument);
    }

    @Override
    protected void checkOperatorArguments() {
        int size = getArguments().size();

        if (size != 1) {
            throw new WrongNumberOfArgumentsException(size, 1, 1);
        }
    }

    @Override
    protected Class execute() {
        String className = getClassName();

        try {
            return Class.forName(DOMAIN_PREFIX + className);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e1) {
                throw new InvalidClassNameSpecified(DOMAIN_PREFIX, className);
            }
        }
    }

    protected String getClassName() {
        return String.valueOf(getArguments().get(CLASS_NAME).getValue());
    }

    /**
     * Since the class operator only translates the value of it's firts argument
     * into a class the dynamic nature of this operator depends on the dynamic
     * nature of it's argument.
     * 
     * @return <code>true</code> if the argument is dynamic and no properties
     *         need to be applied to the operator's value.
     * 
     * @exception WrongNumberOfArgumentsException
     *                if the number of arguments for this operator is not the
     *                correct one
     */
    @Override
    public boolean isDynamic() {
        checkOperatorArguments();

        return getArguments().get(0).isDynamic();
    }

}
