package net.sourceforge.fenixedu.domain.candidacyProcess;

import pt.ist.bennu.core.domain.Bennu;

public class IndividualCandidacySeriesGrade extends IndividualCandidacySeriesGrade_Base {

    public IndividualCandidacySeriesGrade() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    @Deprecated
    public boolean hasNotes() {
        return getNotes() != null;
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
    public boolean hasIndividualCandidacy() {
        return getIndividualCandidacy() != null;
    }

    @Deprecated
    public boolean hasDegreeNature() {
        return getDegreeNature() != null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

    @Deprecated
    public boolean hasAffinity() {
        return getAffinity() != null;
    }

}
