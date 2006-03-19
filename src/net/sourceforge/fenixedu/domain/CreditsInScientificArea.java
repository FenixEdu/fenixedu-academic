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
		removeScientificArea();
		removeEnrolment();
		removeStudentCurricularPlan();
		super.deleteDomainObject();
	}
    
}
