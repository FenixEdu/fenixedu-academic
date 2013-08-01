package net.sourceforge.fenixedu.domain;

import pt.ist.bennu.core.domain.Bennu;

public class DegreeInfoFuture extends DegreeInfoFuture_Base {

    protected DegreeInfoFuture(final DegreeInfo degreeInfo) {
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
    public boolean hasDesignedFor() {
        return getDesignedFor() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasClassifications() {
        return getClassifications() != null;
    }

    @Deprecated
    public boolean hasObjectives() {
        return getObjectives() != null;
    }

    @Deprecated
    public boolean hasDegreeInfo() {
        return getDegreeInfo() != null;
    }

    @Deprecated
    public boolean hasQualificationLevel() {
        return getQualificationLevel() != null;
    }

    @Deprecated
    public boolean hasProfessionalExits() {
        return getProfessionalExits() != null;
    }

    @Deprecated
    public boolean hasRecognitions() {
        return getRecognitions() != null;
    }

}
