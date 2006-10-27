package net.sourceforge.fenixedu.persistenceTier.Conversores;

import java.util.UUID;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaUUID2SqlVarcharFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) throws ConversionException {
        if (source == null) {
            return null;
        }
        else {
            return ((UUID) source).toString();
        }
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source == null) {
            return null;
        }
        else {
            return UUID.fromString((String) source);
        }
    }

}
