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
import java.util.Calendar;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class Calendar2TimeFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof Calendar) {
            return new java.sql.Time(((Calendar) source).getTime().getTime());
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if (source instanceof java.sql.Time) {
            Calendar res = Calendar.getInstance();
            res.setTime(new java.util.Date(((java.sql.Time) source).getTime()));
            return res;
        }

        return source;

    }

}