package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.ExpressionGroup;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * Converts between {@link Group} and {@link String} by converting groups into
 * an expression and compiling an expression into a group.
 * 
 * @see Group#getExpression()
 * @see ExpressionGroup#ExpressionGroup(String)
 * 
 * @author cfgi
 */
public class Group2StringConverter implements FieldConversion {

    /**
     * Default serialization id.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Convert a {@link Group} into a string by asking the group for the 
     * correspondent expression.
     * 
     * @see Group#getExpression()
     */
    public Object javaToSql(Object source) throws ConversionException {
        if (source == null) {
            return null;
        }
        else {
            return ((Group) source).getExpression();
        }
    }

    /**
     * Converts a string into a {@link Group} by compiling the string as 
     * a group expression.
     * 
     * @see ExpressionGroup#ExpressionGroup(String)
     */
    public Object sqlToJava(Object source) throws ConversionException {
        if (source == null) {
            return null;
        }
        else {
            return new ExpressionGroup((String) source).getGroup();
        }
    }

}
