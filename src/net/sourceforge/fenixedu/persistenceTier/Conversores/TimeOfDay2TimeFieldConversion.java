package net.sourceforge.fenixedu.persistenceTier.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;
import org.joda.time.TimeOfDay;

public class TimeOfDay2TimeFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
	if (source instanceof TimeOfDay) {
	    return new java.sql.Time(((TimeOfDay) source).toDateTimeToday().getMillis());
	}
	return source;

    }

    public Object sqlToJava(Object source) {
	if (source instanceof java.sql.Time) {
	    return new TimeOfDay(((java.sql.Time) source).getTime());
	}
	return source;
    }
}