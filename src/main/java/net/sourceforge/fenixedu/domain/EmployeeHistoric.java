package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author T�nia Pous�o
 */
public class EmployeeHistoric extends EmployeeHistoric_Base {

    public EmployeeHistoric() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    @Deprecated
    public java.util.Date getBeginDate() {
        org.joda.time.YearMonthDay ymd = getBeginDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setBeginDate(java.util.Date date) {
        if (date == null) {
            setBeginDateYearMonthDay(null);
        } else {
            setBeginDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEndDate() {
        org.joda.time.YearMonthDay ymd = getEndDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setEndDate(java.util.Date date) {
        if (date == null) {
            setEndDateYearMonthDay(null);
        } else {
            setEndDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getWhen() {
        org.joda.time.DateTime dt = getWhenDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setWhen(java.util.Date date) {
        if (date == null) {
            setWhenDateTime(null);
        } else {
            setWhenDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public boolean hasBeginDateYearMonthDay() {
        return getBeginDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasWhenDateTime() {
        return getWhenDateTime() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasWho() {
        return getWho() != null;
    }

    @Deprecated
    public boolean hasEndDateYearMonthDay() {
        return getEndDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasMailingCostCenter() {
        return getMailingCostCenter() != null;
    }

    @Deprecated
    public boolean hasCalendar() {
        return getCalendar() != null;
    }

    @Deprecated
    public boolean hasResponsableEmployee() {
        return getResponsableEmployee() != null;
    }

    @Deprecated
    public boolean hasWorkingPlaceCostCenter() {
        return getWorkingPlaceCostCenter() != null;
    }

    @Deprecated
    public boolean hasSalaryCostCenter() {
        return getSalaryCostCenter() != null;
    }

    @Deprecated
    public boolean hasEmployee() {
        return getEmployee() != null;
    }

}
