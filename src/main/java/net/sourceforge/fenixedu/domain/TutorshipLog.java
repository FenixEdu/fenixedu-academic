package net.sourceforge.fenixedu.domain;

import pt.ist.bennu.core.domain.Bennu;

public class TutorshipLog extends TutorshipLog_Base {

    public TutorshipLog() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setTutorship(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasTutorship() {
        return getTutorship() != null;
    }

    @Deprecated
    public boolean hasCountsWithSupport() {
        return getCountsWithSupport() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasMotivation() {
        return getMotivation() != null;
    }

    @Deprecated
    public boolean hasRelativesSupport() {
        return getRelativesSupport() != null;
    }

    @Deprecated
    public boolean hasWishesTutor() {
        return getWishesTutor() != null;
    }

    @Deprecated
    public boolean hasAnnotations() {
        return getAnnotations() != null;
    }

    @Deprecated
    public boolean hasDifficultiesOrSpecialLimitations() {
        return getDifficultiesOrSpecialLimitations() != null;
    }

    @Deprecated
    public boolean hasSpaceToValidateStudentsRegistration() {
        return getSpaceToValidateStudentsRegistration() != null;
    }

    @Deprecated
    public boolean hasOptionNumberDegree() {
        return getOptionNumberDegree() != null;
    }

    @Deprecated
    public boolean hasHowManyReunions() {
        return getHowManyReunions() != null;
    }

}
