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

    protected IPeriod nextPeriod;

    public Period() {
    }

    public Period(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public Period(Calendar startDate, Calendar endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
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

    public IPeriod getNextPeriod() {
        return nextPeriod;
    }

    public void setNextPeriod(IPeriod nextPeriod) {
        this.nextPeriod = nextPeriod;
    }

    public Calendar getEndDateOfComposite() {
        Calendar end = this.endDate;
        IPeriod period = this.nextPeriod;
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