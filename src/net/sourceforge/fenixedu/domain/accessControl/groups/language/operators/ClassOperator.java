package net.sourceforge.fenixedu.domain.accessControl.groups.language.operators;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupContextProvider;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.OperatorArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
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

    /**
     * Creates a new <code>ClassOperator</code> representing the given class.
     * This constructor is usefull when you need an external representation of
     * the operator.
     * 
     * @param type
     *            the type this operator will be representing
     */
    public ClassOperator(Class type) {
        this(new StaticArgument(simplify(type.getName())));
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
            if (className.startsWith(DOMAIN_PREFIX)) {
                return Class.forName(className);
            }
            else {
                try {
                    return Class.forName(DOMAIN_PREFIX + className);
                } catch (ClassNotFoundException e) {
                    
                        return Class.forName(className);
                }
            }
        } catch (ClassNotFoundException e1) {
            throw new InvalidClassNameSpecified(DOMAIN_PREFIX, className);
        }
    }

    protected String getClassName() {
        return String.valueOf(argument(CLASS_NAME).getValue());
    }

    /**
     * Simplifies the name of the type based on the prefix {@value #DOMAIN_PREFIX}.
     * 
     * @param typeName the type name to simplify
     * @return the symplified name of the type
     */
    public static String simplify(String typeName) {
        if (typeName.startsWith(DOMAIN_PREFIX)) {
            return typeName.substring(DOMAIN_PREFIX.length());
        }
        else {
            return typeName;
        }
    }
    
    @Override
    public boolean isDynamic() {
        checkOperatorArguments();

        return argument(CLASS_NAME).isDynamic();
    }

    @Override
    protected String getMainValueString() {
        return String.format("$C(%s)", argument(CLASS_NAME));
    }

}
