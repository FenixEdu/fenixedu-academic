/**
 * @author naat
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.accounting.EventState;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class EventState2SqlVarcharFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        return (source instanceof EventState) ? ((EventState) source).name() : null;
    }

    public Object sqlToJava(Object source) throws ConversionException {
        return (source instanceof String) ? EventState.valueOf((String) source) : null;
    }

}
