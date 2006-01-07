/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.domain.credits;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;

/**
 * @author jpvl
 */
public class OtherTypeCreditLine extends OtherTypeCreditLine_Base {

    protected CreditsEvent getCreditEventGenerated() {
        return CreditsEvent.OTHER_CREDIT;
    }
    
    public boolean belongsToExecutionPeriod(ExecutionPeriod executionPeriod) {
        return this.getExecutionPeriod().equals(executionPeriod);
    }

}
