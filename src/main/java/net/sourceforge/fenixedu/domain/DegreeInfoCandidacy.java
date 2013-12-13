package net.sourceforge.fenixedu.domain;

import pt.ist.bennu.core.domain.Bennu;

public class DegreeInfoCandidacy extends DegreeInfoCandidacy_Base {

    protected DegreeInfoCandidacy(final DegreeInfo degreeInfo) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setDegreeInfo(degreeInfo);
    }

    public void delete() {
        setDegreeInfo(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSelectionResultDeadline() {
        return getSelectionResultDeadline() != null;
    }

    @Deprecated
    public boolean hasDegreeInfo() {
        return getDegreeInfo() != null;
    }

    @Deprecated
    public boolean hasCandidacyDocuments() {
        return getCandidacyDocuments() != null;
    }

    @Deprecated
    public boolean hasCandidacyPeriod() {
        return getCandidacyPeriod() != null;
    }

    @Deprecated
    public boolean hasEnrolmentPeriod() {
        return getEnrolmentPeriod() != null;
    }

    @Deprecated
    public boolean hasAccessRequisites() {
        return getAccessRequisites() != null;
    }

    @Deprecated
    public boolean hasTestIngression() {
        return getTestIngression() != null;
    }

}
