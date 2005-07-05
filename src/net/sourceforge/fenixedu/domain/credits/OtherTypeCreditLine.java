/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.domain.credits;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;

/**
 * @author jpvl
 */
public class OtherTypeCreditLine extends OtherTypeCreditLine_Base {

    protected CreditsEvent getCreditEventGenerated() {
        return CreditsEvent.OTHER_CREDIT;
    }
    
    protected void notifyTeacher() {
        ITeacher teacher = this.getTeacher();
        teacher.notifyCreditsChange(getCreditEventGenerated(), this);
    }
    
    public boolean belongsToExecutionPeriod(IExecutionPeriod executionPeriod) {
        return this.getExecutionPeriod().equals(executionPeriod);
    }

}
