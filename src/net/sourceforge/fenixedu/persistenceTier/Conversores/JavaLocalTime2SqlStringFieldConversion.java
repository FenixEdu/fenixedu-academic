package net.sourceforge.fenixedu.persistenceTier.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;
import org.joda.time.LocalTime;

public class JavaLocalTime2SqlStringFieldConversion implements FieldConversion {

    public Object javaToSql(Object source) {
	if (source instanceof LocalTime) {
	    return new java.sql.Time(((LocalTime) source).toDateTimeToday().getMillis());
	}
	return source;

    }

    public Object sqlToJava(Object source) {
	if (source instanceof java.sql.Time) {
	    return new LocalTime(((java.sql.Time) source).getTime());
	}
	return source;
    }

}