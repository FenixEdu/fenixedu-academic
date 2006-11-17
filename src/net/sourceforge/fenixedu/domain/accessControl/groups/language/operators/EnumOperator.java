package net.sourceforge.fenixedu.domain.accessControl.groups.language.operators;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.OperatorArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.InvalidEnumSpecified;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongNumberOfArgumentsException;

/**
 * The <code>$E</code> operator converts a parameter value into a enum. You
 * must also specify the type of the enum. You can also type the enum name
 * directly when the value is known in advance.
 * 
 * <p>
 * If the request contains <code>role=MANAGER&gender=FEMALE</code> then
 * 
 * <code>$E(MANAGER, 'person.RoleType') = MANAGER</code> and
 * <code>$E($P(role), 'person.RoleType') = MANAGER</code> and
 * <code>$E($P(gender), 'person.Gender') = FEMALE</code>
 * 
 * @author cfgi
 */
public class EnumOperator extends OperatorArgument {

    private static final long serialVersionUID = 1L;

    private static final int VALUE = 0;
    private static final int TYPE = 1;

    private ClassOperator type;

    public EnumOperator(Argument name, Argument type) {
        super();
        
        addArgument(name);
        addArgument(type);
    }

    public EnumOperator(Enum value) {
        this(new StaticArgument(value.name()), new StaticArgument(ClassOperator.simplify(value
                .getDeclaringClass().getName())));
    }

    @Override
    protected void checkOperatorArguments() {
        int size = getArguments().size();

        if (size != 2) {
            throw new WrongNumberOfArgumentsException(size, 2, 2);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Enum execute() {
        String enumName = getEnumName();
        Class typeName = getEnumType();

        try {
            return Enum.valueOf(typeName, enumName);
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumSpecified(enumName, typeName);
        }
    }

    /**
     * @return the name of the enum value
     */
    protected String getEnumName() {
        return String.valueOf(argument(VALUE).getValue());
    }

    /**
     * @return the name of the enum type
     */
    protected Class getEnumType() {
        if (this.type == null) {
            this.type = new ClassOperator(this, argument(TYPE));
        }

        return (Class) this.type.getValue();
    }

    @Override
    public String getMainValueString() {
        return String.format("$E(%s, %s)", argument(VALUE), argument(TYPE));
    }
    
}
