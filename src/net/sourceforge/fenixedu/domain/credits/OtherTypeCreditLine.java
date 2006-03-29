package net.sourceforge.fenixedu.domain.credits;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;

public class OtherTypeCreditLine extends OtherTypeCreditLine_Base {

    public OtherTypeCreditLine() {
		super();
	}

	protected CreditsEvent getCreditEventGenerated() {
        return CreditsEvent.OTHER_CREDIT;
    }
    
    public boolean belongsToExecutionPeriod(ExecutionPeriod executionPeriod) {
        return this.getExecutionPeriod().equals(executionPeriod);
    }

    public void delete() {
        super.deleteDomainObject();
    }

}
