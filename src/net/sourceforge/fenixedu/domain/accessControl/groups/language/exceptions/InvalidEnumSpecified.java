package net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions;

/**
 * Thrown when the {@link net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.EnumOperator} 
 * cannot find the enum value.
 * 
 * @author cfgi
 */
public class InvalidEnumSpecified extends GroupDynamicExpressionException {

    private static final long serialVersionUID = 1L;

    private static final String MESSAGE = "accessControl.group.expression.operator.enum.invalidName";
    
    private String name;
    private Class type;

    public InvalidEnumSpecified(String name, Class type) {
        super(MESSAGE, name, type.getName());
        
        this.name = name;
        this.type = type;
    }

    /**
     * @return the name of the enum given
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the type aked for the enum value
     */
    public Class getType() {
        return this.type;
    }

}
