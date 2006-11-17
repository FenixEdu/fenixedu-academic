package net.sourceforge.fenixedu.domain.accessControl.groups.language.operators;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupContextProvider;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.OperatorArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.NumberTypeNotSupported;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongNumberOfArgumentsException;

/**
 * The <code>$N</code> operator converts value into a number.
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
 * <code>$N(1) = 1</code> and
 * <code>$N($P(section)) = 1</code> and
 * <code>$N($P(grade), float) = 16.5</code>
 * 
 * @author cfgi
 */
public class NumberOperator extends OperatorArgument {

    private static final long serialVersionUID = 1L;

    private static final int NUMBER = 0;
    private static final int TYPE = 1;

    protected static enum SupportedType {
        _short, _int, _long, _float, _double
    }

    public NumberOperator(Argument number) {
        super();

        addArgument(number);
    }

    public NumberOperator(Argument number, Argument type) {
        super();
        
        addArgument(number);
        addArgument(type);
    }

    public NumberOperator(Integer number) {
        this(new StaticArgument(number));
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
        String numberValue = getNumberValue();
        return convertNumber(numberValue.toString());
    }

    /**
     * @return the value of the first argument
     */
    protected String getNumberValue() {
        return String.valueOf(argument(NUMBER).getValue());
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
        if (value == null || value.length() == 0) {
            return null;
        }
        
        SupportedType type = getGivenType();

        if (type == null) {
            type = SupportedType._int;
        }

        try {
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
        } catch (NumberFormatException e) {
            throw new GroupDynamicExpressionException("accessControl.group.expression.operator.number.invalid", value);
        }
    }

    /**
     * @return the given type as a value of {@link SupportedType}
     */
    protected SupportedType getGivenType() {
        if (getArguments().size() == 1) {
            return null;
        }

        String type = String.valueOf(argument(TYPE).getValue());
        
        try {
            return SupportedType.valueOf("_" + type);
        } catch (IllegalArgumentException e) {
            throw new NumberTypeNotSupported(type);
        }
    }
    
    @Override
    public String getMainValueString() {
        SupportedType type = getGivenType();
        
        if (type == null || type.equals(SupportedType._int)) {
            return String.format("$N(%s)", argument(NUMBER));
        }
        else {
            return String.format("$N(%s, '%s')", argument(NUMBER), type.name().substring(1));
        }
    }
}
