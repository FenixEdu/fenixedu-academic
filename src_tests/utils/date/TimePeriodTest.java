/*
 * Created on Dec 26, 2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.utils.date;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import net.sourceforge.fenixedu.util.date.TimePeriod;

/**
 * @author jpvl
 */
public class TimePeriodTest extends TestCase {

    /**
     * @param arg0
     */
    public TimePeriodTest(String testName) {
        super(testName);
    }

    public void testHalfHour() {
        Date startDate = getDate(3, 30);
        Date endDate = getDate(4, 0);
        TimePeriod timePeriod = new TimePeriod(startDate, endDate);
        assertEquals("half hour!", 0.5, timePeriod.hours().doubleValue(), 0);
    }

    public void testHourAndAHalf() {
        Date startDate = getDate(3, 30);
        Date endDate = getDate(5, 0);
        TimePeriod timePeriod = new TimePeriod(startDate, endDate);
        assertEquals("hour and half!", 1.5, timePeriod.hours().doubleValue(), 0);
    }

    public void testQuarterOfHour() {
        Date startDate = getDate(3, 15);
        Date endDate = getDate(3, 30);
        TimePeriod timePeriod = new TimePeriod(startDate, endDate);
        assertEquals("Quarter of hour!", 0.25, timePeriod.hours().doubleValue(), 0);
    }

    public void testOne() {
        Date startDate = getDate(10, 30);
        Date endDate = getDate(11, 45);
        TimePeriod timePeriod = new TimePeriod(startDate, endDate);
        assertEquals("Quarter of hour!", 1.25, timePeriod.hours().doubleValue(), 0);
    }

    /**
     * @param i
     * @param j
     * @return
     */
    private Date getDate(int hours, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
}