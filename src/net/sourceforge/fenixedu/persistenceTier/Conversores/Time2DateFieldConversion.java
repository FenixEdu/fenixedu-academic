/*
 * Calendar2TimeFieldConversion.java
 * 
 * Created on October 13, 2002, 00:42 PM
 */

package net.sourceforge.fenixedu.persistenceTier.Conversores;

/**
 * @author jpvl
 */
import java.sql.Time;
import java.util.Date;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class Time2DateFieldConversion implements FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof Date) {
            Date date = (Date) source;
            Time time = new Time(date.getTime());
            return time;
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if (source instanceof Time) {
            Time time = (Time) source;
            Date date = new Date(time.getTime());
            return date;
        }

        return source;

    }

}