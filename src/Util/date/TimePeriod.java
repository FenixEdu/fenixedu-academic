/*
 * Created on Dec 26, 2003 by jpvl
 *  
 */
package Util.date;

import java.util.Calendar;
import java.util.Date;

import Util.FenixUtil;

/**
 * @author jpvl
 */
public class TimePeriod extends FenixUtil
{
    private long start;

    private long end;

    public TimePeriod( long start, long end )
    {
        this.start = start;
        this.end = end;
    }

    /**
     *  
     */
    public TimePeriod( Date start, Date end )
    {
        this(start.getTime(), end.getTime());
    }

    public TimePeriod( Calendar start, Calendar end )
    {
        this(start.getTimeInMillis(), end.getTimeInMillis());
    }

    public Double hours()
    {
        Calendar endCalendar = Calendar.getInstance();

        endCalendar.setTimeInMillis(this.end);

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(start);

        int endMinutes = endCalendar.get(Calendar.MINUTE);
        int startMinutes = startCalendar.get(Calendar.MINUTE);
       
        endCalendar.roll(Calendar.HOUR_OF_DAY, -(startCalendar.get(Calendar.HOUR_OF_DAY)));
        endCalendar.roll(Calendar.MINUTE, -(startCalendar.get(Calendar.MINUTE)));
        
        int minutes = endCalendar.get(Calendar.MINUTE);
        int hours = endMinutes < startMinutes ? endCalendar.get(Calendar.HOUR_OF_DAY) - 1 : endCalendar.get(Calendar.HOUR_OF_DAY);

        double minutesInHours = minutes / 60.0;

        return new Double(hours + minutesInHours);
    }
}
