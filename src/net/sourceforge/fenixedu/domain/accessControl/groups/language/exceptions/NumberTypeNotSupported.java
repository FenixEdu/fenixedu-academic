package net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions;

/**
 * Thrown when an invlaid type is specified for th {@link net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.NumberOperator}.
 * 
 * @author cfgi
 */
public class NumberTypeNotSupported extends GroupDynamicExpressionException {

    private static final long serialVersionUID = 1L;

    private static final String MESSAGE = "accessControl.group.expression.operator.number.invalidType";
    
    private String type;

    public NumberTypeNotSupported(String type) {
        super(MESSAGE, type);
        
        this.type = type;
    }

    /**
     * @return the given type
     */
    public String getType() {
        return this.type;
    }
    
}
