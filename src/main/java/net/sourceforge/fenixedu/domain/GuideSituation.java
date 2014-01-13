package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.util.State;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class GuideSituation extends GuideSituation_Base {

    public GuideSituation() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public GuideSituation(GuideState situation, String remarks, Date date, Guide guide, State state) {
        this();
        this.setRemarks(remarks);
        this.setGuide(guide);
        this.setSituation(situation);
        this.setDate(date);
        this.setState(state);
    }

    public void delete() {
        setGuide(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public java.util.Date getDate() {
        org.joda.time.YearMonthDay ymd = getDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setDate(java.util.Date date) {
        if (date == null) {
            setDateYearMonthDay(null);
        } else {
            setDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasState() {
        return getState() != null;
    }

    @Deprecated
    public boolean hasGuide() {
        return getGuide() != null;
    }

    @Deprecated
    public boolean hasRemarks() {
        return getRemarks() != null;
    }

    @Deprecated
    public boolean hasDateYearMonthDay() {
        return getDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasSituation() {
        return getSituation() != null;
    }

}
