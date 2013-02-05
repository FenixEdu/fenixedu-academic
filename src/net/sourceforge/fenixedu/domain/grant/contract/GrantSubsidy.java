package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class GrantSubsidy extends GrantSubsidy_Base {

    public GrantSubsidy() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        for (; !getAssociatedGrantParts().isEmpty(); getAssociatedGrantParts().get(0).delete()) {
            ;
        }
        removeGrantContract();
        removeRootDomainObject();
        super.deleteDomainObject();
    }

    @Deprecated
    public java.util.Date getDateBeginSubsidy() {
        org.joda.time.YearMonthDay ymd = getDateBeginSubsidyYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setDateBeginSubsidy(java.util.Date date) {
        if (date == null) {
            setDateBeginSubsidyYearMonthDay(null);
        } else {
            setDateBeginSubsidyYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getDateEndSubsidy() {
        org.joda.time.YearMonthDay ymd = getDateEndSubsidyYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setDateEndSubsidy(java.util.Date date) {
        if (date == null) {
            setDateEndSubsidyYearMonthDay(null);
        } else {
            setDateEndSubsidyYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

}
