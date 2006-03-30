package net.sourceforge.fenixedu.domain.credits;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
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

    public static List<OtherTypeCreditLine> readByTeacherAndExecutionPeriod(Teacher teacher, ExecutionPeriod executionPeriod) {
        List<OtherTypeCreditLine> result = new ArrayList<OtherTypeCreditLine>();
        
        for (OtherTypeCreditLine otherTypeCreditLine : teacher.getOtherTypeCreditLines()) {
            if (otherTypeCreditLine.getExecutionPeriod().equals(executionPeriod)) {
                result.add(otherTypeCreditLine);
            }
        }
        
        return result;
    }
    
}
