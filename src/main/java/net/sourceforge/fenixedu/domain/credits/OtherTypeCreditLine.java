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

    @Override
    protected CreditsEvent getCreditEventGenerated() {
        return CreditsEvent.OTHER_CREDIT;
    }

    @Override
    public boolean belongsToExecutionPeriod(ExecutionSemester executionSemester) {
        return this.getExecutionPeriod().equals(executionSemester);
    }

    public void delete() {
        setRootDomainObject(null);
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

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
    }

    @Deprecated
    public boolean hasCredits() {
        return getCredits() != null;
    }

    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasReason() {
        return getReason() != null;
    }

}
