/*
 * Created on 3/Dez/2003
 *
 */
package Util;

import java.util.Calendar;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @author Ana e Ricardo
 *
 */
public class CalendarUtil
{	
    
    public CalendarUtil(){
	}

	public static boolean intersectDates(Calendar startDate1, Calendar endDate1, Calendar startDate2, Calendar endDate2) {
		String startDate1String = date2string(startDate1);
		String endDate1String = date2string(endDate1);
		String startDate2String = date2string(startDate2);
		String endDate2String = date2string(endDate2);
		
		boolean doesNotIntersect =
			(endDate2String.compareTo(startDate1String) < 0)
			|| (startDate2String.compareTo(endDate1String) > 0);

		return !doesNotIntersect;
	}

    private static String date2string(Calendar date) {
		return DateFormatUtils.format(date.getTime(), "yyyyMMdd");
    }

	public static boolean intersectTimes(Calendar startTime1, Calendar endTime1, Calendar startTime2, Calendar endTime2) {
		String startTime1String = time2string(startTime1);
		String endTime1String = time2string(endTime1);
		String startTime2String = time2string(startTime2);
		String endTime2String = time2string(endTime2);
		boolean doesNotIntersect =
			(endTime2String.compareTo(startTime1String) <= 0)
			|| (startTime2String.compareTo(endTime1String) >= 0);
		return !doesNotIntersect;
	}

	private static String time2string(Calendar time) {
		return DateFormatUtils.format(time.getTime(), "HHmmss");
	}


 }
