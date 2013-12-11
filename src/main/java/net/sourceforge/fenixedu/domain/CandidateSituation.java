package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;
import pt.ist.bennu.core.domain.Bennu;

public class CandidateSituation extends CandidateSituation_Base {

    public CandidateSituation() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public CandidateSituation(Date date, String remarks, State validation, MasterDegreeCandidate masterDegreeCandidate,
            SituationName situation) {

        this();
        setMasterDegreeCandidate(masterDegreeCandidate);
        setSituation(situation);
        setDate(date);
        setRemarks(remarks);
        setValidation(validation);
    }

    public void delete() {
        setMasterDegreeCandidate(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
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
    public boolean hasMasterDegreeCandidate() {
        return getMasterDegreeCandidate() != null;
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

    @Deprecated
    public boolean hasValidation() {
        return getValidation() != null;
    }

}
