package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class GuideSituation extends GuideSituation_Base {

    public GuideSituation() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
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
        removeGuide();
        removeRootDomainObject();
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

}
