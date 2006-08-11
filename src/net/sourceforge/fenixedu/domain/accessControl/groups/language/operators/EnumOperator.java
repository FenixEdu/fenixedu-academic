package net.sourceforge.fenixedu.domain.accessControl.groups.language.operators;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.OperatorArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.InvalidEnumSpecified;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongNumberOfArgumentsException;

/**
 * The <code>$E</code> operator converts a parameter value into a enum. You
 * must also specify the type of the enum.
 * 
 * <p>
 * If the request contains <code>role=MANAGER&gender=FEMALE</code> then
 * 
 * <code>$E(role, 'person.RoleType') = MANAGER</code> and
 * <code>$E(gender, 'person.Gender') = FEMALE</code>
 * 
 * @author cfgi
 */
public class EnumOperator extends OperatorArgument {

    private static final long serialVersionUID = 1L;

    private static final int PARAMETER = 0;
    private static final int TYPE = 1;

    private ParameterOperator parameter;
    private ClassOperator type;

    public EnumOperator(Argument name) {
        super();
        
        addArgument(name);
    }

    public EnumOperator(Argument name, Argument type) {
        super();
        
        addArgument(name);
        addArgument(type);
    }

    @Override
    protected void checkOperatorArguments() {
        int size = getArguments().size();

        if (size < 1 | size > 2) {
            throw new WrongNumberOfArgumentsException(size, 1, 2);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Enum execute() {
        ParameterOperator parameter = getParameterOperator();
        ClassOperator type = getClassOperator();
        
        Class typeName = (Class) type.getValue();
        String enumName = String.valueOf(parameter.getValue());

        try {
            return Enum.valueOf(typeName, enumName);
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumSpecified(enumName, typeName);
        }
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
     * @return the ClassOperator that will obtain the type
     */
    protected ClassOperator getClassOperator() {
        if (this.type == null) {
            this.type = new ClassOperator(this, getArguments().get(TYPE));
        }

        return this.type;
    }
}
