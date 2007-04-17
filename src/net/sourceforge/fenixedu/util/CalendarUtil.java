/*
 * Created on 3/Dez/2003
 *
 */
package net.sourceforge.fenixedu.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @author Ana e Ricardo
 * @author Barbosa e Pica
 * 
 */
public class CalendarUtil {

    public CalendarUtil() {
    }

    public static boolean intersectDates(Calendar startDate1, Calendar endDate1, Calendar startDate2, Calendar endDate2) {
        String startDate1String = date2string(startDate1);
        String endDate1String = date2string(endDate1);
        String startDate2String = date2string(startDate2);
        String endDate2String = date2string(endDate2);

        boolean doesNotIntersect = (endDate2String.compareTo(startDate1String) < 0)
                || (startDate2String.compareTo(endDate1String) > 0);

        return !doesNotIntersect;
    }

    public static boolean equalDates(Calendar date1, Calendar date2) {
        String date1String = date2string(date1);
        String date2String = date2string(date2);

        return date1String.equals(date2String);
    }

    public static String date2string(Calendar date) {
        return DateFormatUtils.format(date.getTime(), "yyyyMMdd");
    }

    public static boolean intersectTimes(Date startTime1, Date endTime1, Date startTime2, Date endTime2) {
        String startTime1String = time2string(startTime1);
        String endTime1String = time2string(endTime1);
        String startTime2String = time2string(startTime2);
        String endTime2String = time2string(endTime2);
        
        boolean doesNotIntersect = (endTime2String.compareTo(startTime1String) <= 0)
                || (startTime2String.compareTo(endTime1String) >= 0);
        
        return !doesNotIntersect;
    }

    public static boolean intersectTimes(Calendar startTime1, Calendar endTime1, Calendar startTime2,
            Calendar endTime2) {
        return intersectTimes(startTime1.getTime(), endTime1.getTime(), startTime2.getTime(), endTime2.getTime());
    }

    public static String time2string(Date time) {
        return DateFormatUtils.format(time, "HHmmss");
    }

    public static Integer getNumberOfDaysBetweenDates(Date beginDate, Date endDate) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(beginDate);
        c2.setTime(endDate);

        long timeBetweenInMillis = c2.getTimeInMillis() - c1.getTimeInMillis();
        Integer numberDaysBetweenDates = new Integer(new Long(timeBetweenInMillis / (3600000 * 24))
                .intValue());

        return numberDaysBetweenDates;
    }

}