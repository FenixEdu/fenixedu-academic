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
        if (this.getStart() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getStart());
            return result;
        }
        return null;
    }

    /**
     * @param calendar
     */
    public void setEndDate(Calendar calendar) {
        if (calendar != null) {
            this.setEnd(calendar.getTime());
        } else {
            this.setEnd(null);
        }
    }

    /**
     * @return
     */
    public Calendar getEndDate() {
        if (this.getEnd() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnd());
            return result;
        }
        return null;
    }

    /**
     * @param calendar
     */
    public void setStartDate(Calendar calendar) {
        if (calendar != null) {
            this.setStart(calendar.getTime());    
        } else {
            this.setStart(null);
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof IPeriod) {
            final IPeriod period = (IPeriod) obj;
            return this.getIdInternal().equals(period.getIdInternal());
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
