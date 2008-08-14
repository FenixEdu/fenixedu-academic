package net.sourceforge.fenixedu.domain.credits;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;

public class OtherTypeCreditLine extends OtherTypeCreditLine_Base {

    public OtherTypeCreditLine() {
	super();
    }

    protected CreditsEvent getCreditEventGenerated() {
	return CreditsEvent.OTHER_CREDIT;
    }

    public boolean belongsToExecutionPeriod(ExecutionSemester executionSemester) {
	return this.getExecutionPeriod().equals(executionSemester);
    }

    public void delete() {
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public static List<OtherTypeCreditLine> readByTeacherAndExecutionPeriod(Teacher teacher, ExecutionSemester executionSemester) {
	List<OtherTypeCreditLine> result = new ArrayList<OtherTypeCreditLine>();

	for (OtherTypeCreditLine otherTypeCreditLine : teacher.getOtherTypeCreditLines()) {
	    if (otherTypeCreditLine.getExecutionPeriod().equals(executionSemester)) {
		result.add(otherTypeCreditLine);
	    }
	}

	return result;
    }

}
