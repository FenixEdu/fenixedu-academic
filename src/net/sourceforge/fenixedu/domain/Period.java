/*
 * Created on 14/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;

import net.sourceforge.fenixedu.util.CalendarUtil;

/**
 * @author Ana e Ricardo
 * 
 */
public class Period extends Period_Base {

    public Period() {
    }

    public Period(Calendar startDate, Calendar endDate) {
        this.setStartDate(startDate);
        this.setEndDate(endDate);
    }

    /**
     * @return
     */
    public Calendar getStartDate() {
        Calendar result = Calendar.getInstance();
        result.setTime(this.getStart());
        return result;
    }

    /**
     * @param calendar
     */
    public void setEndDate(Calendar calendar) {
        this.setEnd(calendar.getTime());
    }

    /**
     * @return
     */
    public Calendar getEndDate() {
        Calendar result = Calendar.getInstance();
        result.setTime(this.getEnd());
        return result;
    }

    /**
     * @param calendar
     */
    public void setStartDate(Calendar calendar) {
        this.setStart(calendar.getTime());
    }

    public boolean equals(Object obj) {
        if (obj instanceof IPeriod) {
            IPeriod periodObj = (IPeriod) obj;
            if (this.getStartDate().equals(periodObj.getStartDate()) && this.getEndDate().equals(periodObj.getEndDate())) {
                return true;
            }

            return false;

        }
        return false;
    }

    public Calendar getEndDateOfComposite() {
        Calendar end = this.getEndDate();
        IPeriod period = this.getNextPeriod();
        while (period != null) {
            end = period.getEndDate();
            period = period.getNextPeriod();
        }
        return end;
    }

    public boolean intersectPeriods(IPeriod period) {
        while (period != null) {
            IPeriod secondPeriod = this;
            while (secondPeriod != null) {
                if (CalendarUtil.intersectDates(period.getStartDate(), period.getEndDate(), secondPeriod
                        .getStartDate(), secondPeriod.getEndDate())) {
                    return true;
                }
                secondPeriod = secondPeriod.getNextPeriod();
            }
            period = period.getNextPeriod();
        }
        return false;
    }
    
    public boolean containsDay(Calendar day){
        return !(this.getStartDate().after(day) || this.getEndDate().before(day));
    }

}
