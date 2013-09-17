package net.sourceforge.fenixedu.domain;

/**
 * @author David Santos Jan 14, 2004
 */

public class CreditsInScientificArea extends CreditsInScientificArea_Base {

    public CreditsInScientificArea() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        setScientificArea(null);
        setEnrolment(null);
        setStudentCurricularPlan(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public double getEctsCredits() {
        return 7.5;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEnrolment() {
        return getEnrolment() != null;
    }

    @Deprecated
    public boolean hasGivenCredits() {
        return getGivenCredits() != null;
    }

    @Deprecated
    public boolean hasStudentCurricularPlan() {
        return getStudentCurricularPlan() != null;
    }

    @Deprecated
    public boolean hasScientificArea() {
        return getScientificArea() != null;
    }

}
