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
    protected Calendar startDate;

    protected Calendar endDate;

    public Period() {
    }

    public Period(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public Period(Calendar startDate, Calendar endDate) {
        this.setStartDate(startDate);
        this.setEndDate(endDate);
    }

    /**
     * @return
     */
    public Calendar getEndDate() {
        return endDate;
    }

    /**
     * @return
     */
    public Calendar getStartDate() {
        return startDate;
    }

    /**
     * @param calendar
     */
    public void setEndDate(Calendar calendar) {
        endDate = calendar;
    }

    /**
     * @param calendar
     */
    public void setStartDate(Calendar calendar) {
        startDate = calendar;
    }

    public boolean equals(Object obj) {
        if (obj instanceof IPeriod) {
            IPeriod periodObj = (IPeriod) obj;
            if (startDate.equals(periodObj.getStartDate()) && endDate.equals(periodObj.getEndDate())) {
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

}