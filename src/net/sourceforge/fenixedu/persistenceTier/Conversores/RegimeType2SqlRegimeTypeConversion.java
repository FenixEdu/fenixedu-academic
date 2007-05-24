/*
 * Created on Dec 5, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.RegimeType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class RegimeType2SqlRegimeTypeConversion implements FieldConversion{

    public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof RegimeType) {
            RegimeType s = (RegimeType) source;
            return s.name();
        }
        return source;
    }

    public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof String) {            
            String src = (String) source;            
            return RegimeType.valueOf(src);
        }
        return source;
    }

}
