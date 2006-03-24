package net.sourceforge.fenixedu.persistenceTier.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;
import org.joda.time.YearMonthDay;

public class JavaYearMonthDay2SqlStringFieldConversion implements FieldConversion {

    public Object javaToSql(Object object) {
        return object != null ? object.toString() : null;
    }

    public Object sqlToJava(Object object) {
    	if (object != null && object instanceof String) {
    		final String string = (String) object;
            int year = Integer.parseInt(string.substring(0, 4));
            int month = Integer.parseInt(string.substring(5, 7));
            int day = Integer.parseInt(string.substring(8, 10));
			return new YearMonthDay(year, month, day);
        }
        return object;
    }

}