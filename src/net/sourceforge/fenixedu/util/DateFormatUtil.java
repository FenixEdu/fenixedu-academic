package net.sourceforge.fenixedu.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateFormatUtil {

    private static final Map<String, DateFormat> dateFormatMap = new HashMap<String, DateFormat>();

    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    public static DateFormat dateFormat(final String format) {
	final DateFormat dateFormat;
	if (!dateFormatMap.containsKey(format)) {
	    dateFormat = new SimpleDateFormat(format);
	    dateFormatMap.put(format, dateFormat);
	} else {
	    dateFormat = dateFormatMap.get(format);
	}
	return dateFormat;
    }

    public static Date parse(final String format, final String dateString) throws ParseException {
	return dateFormat(format).parse(dateString);
    }

    public static String format(final String format, final Date date) {
	return dateFormat(format).format(date);
    }

    public static int compareDates(final String format, final Date date1, final Date date2) {
	final DateFormat dateFormat = dateFormat(format);
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

    public static boolean isAfter(final String format, final Date date1, final Date date2) {
	return compareDates(format, date1, date2) > 0;
    }

}
