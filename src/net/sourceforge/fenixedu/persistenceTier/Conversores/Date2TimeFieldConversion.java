/*
 * Created on May 4, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import java.util.Date;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author jdnf
 *
 */

public class Date2TimeFieldConversion implements FieldConversion{

    public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof Date) {
            return new java.sql.Time(((Date) source).getTime());
        }
        return source;
    }

  
    public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof java.sql.Time) {
            Date res = new Date();
            res.setTime(((java.sql.Time) source).getTime());
            return res;
        }
        return source;
    }

}
