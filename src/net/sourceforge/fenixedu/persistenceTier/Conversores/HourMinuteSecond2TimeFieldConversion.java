package net.sourceforge.fenixedu.persistenceTier.Conversores;

import java.sql.Time;

import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class HourMinuteSecond2TimeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
	if (source instanceof HourMinuteSecond) {
	    HourMinuteSecond hms = (HourMinuteSecond) source;
	    return new Time(hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
	}
	return source;
    }

    public Object sqlToJava(Object source) {
	if (source == null || source.equals("")) {
	    return null;
	}
	if (source instanceof Time) {
	    Time time = (Time) source;
	    return new HourMinuteSecond(time.getHours(), time.getMinutes(), time.getSeconds());
	}
	if (source instanceof String) {
	    String time = (String) source;
	    return new HourMinuteSecond(Integer.valueOf(time.substring(0, 2)), Integer.valueOf(time.substring(3, 5)), Integer
		    .valueOf(time.substring(6, 8)));
	}
	return source;
    }

}