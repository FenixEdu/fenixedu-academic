package net.sourceforge.fenixedu.domain.credits;

import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;

public class ManagementPositionCreditLine extends ManagementPositionCreditLine_Base {

    public ManagementPositionCreditLine() {
        super();
    }

    @Override
    protected CreditsEvent getCreditEventGenerated() {
        return CreditsEvent.MANAGEMENT_POSITION;
    }

    public void delete() {
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
    }

    @Deprecated
    public boolean hasCredits() {
        return getCredits() != null;
    }

    @Deprecated
    public boolean hasPosition() {
        return getPosition() != null;
    }

}
