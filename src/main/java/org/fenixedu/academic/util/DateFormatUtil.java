package org.fenixedu.academic.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Deprecated
public class DateFormatUtil {

    public static Date parse(final String format, final String dateString) throws ParseException {
        return new SimpleDateFormat(format).parse(dateString);
    }

    public static String format(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static int compareDates(final String format, final Date date1, final Date date2) {
        final DateFormat dateFormat = new SimpleDateFormat(format);
        final String date1String = dateFormat.format(date1);
        final String date2String = dateFormat.format(date2);
        return date1String.compareTo(date2String);
    }

    public static boolean equalDates(final String format, final Date date1, final Date date2) {
        return compareDates(format, date1, date2) == 0;
    }

    public static boolean isBefore(final String format, final Date date1, final Date date2) {
        return compareDates(format, date1, date2) < 0;
    }

}
