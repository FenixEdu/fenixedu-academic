/*
 * Calendar2TimeFieldConversion.java
 *
 * Created on October 13, 2002, 00:42 PM
 */

package net.sourceforge.fenixedu.persistenceTier.Conversores;

/**
 * 
 * @author tfc130
 */
import java.sql.Timestamp;
import java.util.Date;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class TimeStamp2DateFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof Date) {
            return new Timestamp(((Date) source).getTime());
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if (source instanceof Timestamp) {
            return new Date(((Timestamp) source).getTime());
        }

        return source;

    }

}