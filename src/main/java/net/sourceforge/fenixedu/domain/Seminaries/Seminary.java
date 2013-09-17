/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at Jul 23, 2003, 9:49:19 AM
 * 
 */
public class Seminary extends Seminary_Base {

    public Seminary() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    /**
     * @return
     */
    public Calendar getEnrollmentBeginDate() {
        if (this.getEnrollmentBegin() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnrollmentBegin());
            return result;
        }
        return null;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentBeginDate(Calendar calendar) {
        if (calendar != null) {
            this.setEnrollmentBegin(calendar.getTime());
        } else {
            this.setEnrollmentBegin(null);
        }
    }

    /**
     * @return
     */
    public Calendar getEnrollmentBeginTime() {
        if (this.getEnrollmentTimeBegin() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnrollmentTimeBegin());
            return result;
        }
        return null;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentBeginTime(Calendar calendar) {
        if (calendar != null) {
            this.setEnrollmentTimeBegin(calendar.getTime());
        } else {
            this.setEnrollmentTimeBegin(null);
        }

    }

    /**
     * @return
     */
    public Calendar getEnrollmentEndDate() {
        if (this.getEnrollmentEnd() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnrollmentEnd());
            return result;
        }
        return null;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentEndDate(Calendar calendar) {
        if (calendar != null) {
            this.setEnrollmentEnd(calendar.getTime());
        } else {
            this.setEnrollmentEnd(null);
        }
    }

    /**
     * @return
     */
    public Calendar getEnrollmentEndTime() {
        if (this.getEnrollmentTimeEnd() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnrollmentTimeEnd());
            return result;
        }
        return null;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentEndTime(Calendar calendar) {
        if (calendar != null) {
            this.setEnrollmentTimeEnd(calendar.getTime());
        } else {
            this.setEnrollmentTimeEnd(null);
        }
    }

    @Deprecated
    public java.util.Date getEnrollmentBegin() {
        org.joda.time.YearMonthDay ymd = getEnrollmentBeginYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setEnrollmentBegin(java.util.Date date) {
        if (date == null) {
            setEnrollmentBeginYearMonthDay(null);
        } else {
            setEnrollmentBeginYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEnrollmentEnd() {
        org.joda.time.YearMonthDay ymd = getEnrollmentEndYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setEnrollmentEnd(java.util.Date date) {
        if (date == null) {
            setEnrollmentEndYearMonthDay(null);
        } else {
            setEnrollmentEndYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEnrollmentTimeBegin() {
        net.sourceforge.fenixedu.util.HourMinuteSecond hms = getEnrollmentTimeBeginHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setEnrollmentTimeBegin(java.util.Date date) {
        if (date == null) {
            setEnrollmentTimeBeginHourMinuteSecond(null);
        } else {
            setEnrollmentTimeBeginHourMinuteSecond(net.sourceforge.fenixedu.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEnrollmentTimeEnd() {
        net.sourceforge.fenixedu.util.HourMinuteSecond hms = getEnrollmentTimeEndHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setEnrollmentTimeEnd(java.util.Date date) {
        if (date == null) {
            setEnrollmentTimeEndHourMinuteSecond(null);
        } else {
            setEnrollmentTimeEndHourMinuteSecond(net.sourceforge.fenixedu.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy> getCandidacies() {
        return getCandidaciesSet();
    }

    @Deprecated
    public boolean hasAnyCandidacies() {
        return !getCandidaciesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency> getEquivalencies() {
        return getEquivalenciesSet();
    }

    @Deprecated
    public boolean hasAnyEquivalencies() {
        return !getEquivalenciesSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasEnrollmentBeginYearMonthDay() {
        return getEnrollmentBeginYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEnrollmentTimeEndHourMinuteSecond() {
        return getEnrollmentTimeEndHourMinuteSecond() != null;
    }

    @Deprecated
    public boolean hasEnrollmentEndYearMonthDay() {
        return getEnrollmentEndYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasHasCaseStudy() {
        return getHasCaseStudy() != null;
    }

    @Deprecated
    public boolean hasHasTheme() {
        return getHasTheme() != null;
    }

    @Deprecated
    public boolean hasEnrollmentTimeBeginHourMinuteSecond() {
        return getEnrollmentTimeBeginHourMinuteSecond() != null;
    }

    @Deprecated
    public boolean hasAllowedCandidaciesPerStudent() {
        return getAllowedCandidaciesPerStudent() != null;
    }

}
