/*
 * Created on Jun 22, 2006
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.accounting.EventType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaEventType2SqlEventTypeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        return (source instanceof EventType) ? ((EventType) source).name() : null;  
    }

    public Object sqlToJava(Object source) throws ConversionException {
        return (source instanceof String) ? EventType.valueOf((String) source) : null;
    }

}
