package net.sourceforge.fenixedu.domain.credits;

import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;

public class ManagementPositionCreditLine extends ManagementPositionCreditLine_Base {

    public ManagementPositionCreditLine() {
	super();
    }

    protected CreditsEvent getCreditEventGenerated() {
	return CreditsEvent.MANAGEMENT_POSITION;
    }

    public void delete() {
	removeRootDomainObject();
	super.deleteDomainObject();
    }

}
