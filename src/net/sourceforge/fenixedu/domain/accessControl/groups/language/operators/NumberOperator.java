package net.sourceforge.fenixedu.domain.accessControl.groups.language.operators;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupContextProvider;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.OperatorArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.NumberTypeNotSupported;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongNumberOfArgumentsException;

/**
 * The <code>$N</code> operator converts a parameter value into a number.
 * Optionally the operator can receive a second argument indicating the type of
 * the number. Supported types are:
 * 
 * <ul>
 * <li><code>short</code></li>
 * <li><code>int</code></li>
 * <li><code>long</code></li>
 * <li><code>float</code></li>
 * <li><code>double</code></li>
 * </ul>
 * 
 * By default the type is <code>int</code>.
 * 
 * <p>
 * If the request contains <code>section=1&grade=16.5</code> then
 * 
 * <code>$N(section) = 1</code> and
 * <code>$N(grade, float) = 16.5</code>
 * 
 * @author cfgi
 */
public class NumberOperator extends OperatorArgument {

    private static final long serialVersionUID = 1L;

    private static final int PARAMETER = 0;
    private static final int TYPE = 1;

    protected static enum SupportedType {
        _short, _int, _long, _float, _double
    }

    private ParameterOperator parameter;

    public NumberOperator(Argument number) {
        super();

        addArgument(number);
    }

    public NumberOperator(Argument number, Argument type) {
        super();
        
        addArgument(number);
        addArgument(type);
    }

    NumberOperator(GroupContextProvider provider, Argument argument) {
        this(argument);
        
        setContextProvider(provider);
    }


    @Override
    protected void checkOperatorArguments() {
        int size = getArguments().size();

        if (size < 1 | size > 2) {
            throw new WrongNumberOfArgumentsException(size, 1, 2);
        }
    }

    @Override
    protected Number execute() {
        ParameterOperator parameter = getParameterOperator();

        return convertNumber(String.valueOf(parameter.getValue()));
    }

    /**
     * @return the ParameterOperator that will obtain the parameter's value
     */
    protected ParameterOperator getParameterOperator() {
        if (this.parameter == null) {
            this.parameter = new ParameterOperator(this, getArguments().get(PARAMETER));
        }

        return this.parameter;
    }

    /**
     * Converts the parameter value to a number of the appropriate type.
     * 
     * @param value
     *            the value to convert
     * 
     * @return the the value as a number of the asked type
     */
    protected Number convertNumber(String value) {
        SupportedType type = getGivenType();

        if (type == null) {
            type = SupportedType._int;
        }

        switch (type) {
        case _short:
            return new Short(value);
        case _int:
            return new Integer(value);
        case _long:
            return new Byte(value);
        case _float:
            return new Float(value);
        case _double:
            return new Double(value);
        default:
            throw new RuntimeException();
        }
    }

    /**
     * @return the given type as a value of {@link SupportedType}
     */
    protected SupportedType getGivenType() {
        if (getArguments().size() == 1) {
            return null;
        }

        String type = String.valueOf(getArguments().get(TYPE).getValue());
        
        try {
            return SupportedType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new NumberTypeNotSupported(type);
        }
    }

}
