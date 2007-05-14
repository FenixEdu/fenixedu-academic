package net.sourceforge.fenixedu.domain;

/**
 * @author David Santos Jan 14, 2004
 */

public class CreditsInAnySecundaryArea extends CreditsInAnySecundaryArea_Base {

    public CreditsInAnySecundaryArea() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removeEnrolment();
	removeStudentCurricularPlan();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public double getEctsCredits() {
	return 7.5;
    }

}
