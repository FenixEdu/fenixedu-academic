package net.sourceforge.fenixedu.util.date;

import org.joda.time.DateTimeFieldType;
import org.joda.time.YearMonthDay;

public class SerializationTool {

    public static String yearMonthDayDeserialize(final YearMonthDay yearMonthDay) {
        if (yearMonthDay != null) {
            final String dateString = String.format("%d-%02d-%02d", yearMonthDay.get(DateTimeFieldType.year()),
                    yearMonthDay.get(DateTimeFieldType.monthOfYear()), yearMonthDay.get(DateTimeFieldType.dayOfMonth()));
            return dateString.length() != 10 ? null : dateString;
        }
        return null;
    }

    public static YearMonthDay yearMonthDaySerialize(String string) {
	if (string != null) {
            int year = Integer.parseInt(string.substring(0, 4));
            int month = Integer.parseInt(string.substring(5, 7));
            int day = Integer.parseInt(string.substring(8, 10));
            return year == 0 || month == 0 || day == 0 ? null : new YearMonthDay(year, month, day);
        }
        return null;
    }

}
