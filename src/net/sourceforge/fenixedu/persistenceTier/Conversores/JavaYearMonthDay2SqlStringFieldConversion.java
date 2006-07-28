package net.sourceforge.fenixedu.persistenceTier.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;
import org.joda.time.DateTimeFieldType;
import org.joda.time.YearMonthDay;

public class JavaYearMonthDay2SqlStringFieldConversion implements FieldConversion {

    public Object javaToSql(Object object) {
        if (object != null) {
            YearMonthDay yearMonthDay = (YearMonthDay) object;

            String dateString = String.format("%d-%02d-%02d", yearMonthDay.get(DateTimeFieldType.year()),
                    yearMonthDay.get(DateTimeFieldType.monthOfYear()), yearMonthDay
                            .get(DateTimeFieldType.dayOfMonth()));
            
            if(dateString.length() != 10) {
                return null;
            }
            
            return dateString;
        }
        return null;
    }

    public Object sqlToJava(Object object) {
        if (object != null && object instanceof String) {
            final String string = (String) object;
            int year = Integer.parseInt(string.substring(0, 4));
            int month = Integer.parseInt(string.substring(5, 7));
            int day = Integer.parseInt(string.substring(8, 10));
            if (year == 0 || month == 0 || day == 0) {
                return null;
            }
            return new YearMonthDay(year, month, day);
        }
        return object;
    }
}