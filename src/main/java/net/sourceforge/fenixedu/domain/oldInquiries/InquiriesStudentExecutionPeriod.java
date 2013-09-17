package net.sourceforge.fenixedu.domain.oldInquiries;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Student;

public class InquiriesStudentExecutionPeriod extends InquiriesStudentExecutionPeriod_Base {

    public InquiriesStudentExecutionPeriod() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public InquiriesStudentExecutionPeriod(final Student student) {
        this();
        setStudent(student);
        setExecutionPeriod(ExecutionSemester.readActualExecutionSemester());
    }

    public InquiriesStudentExecutionPeriod(final Student student, final ExecutionSemester executionSemester) {
        this();
        setStudent(student);
        setExecutionPeriod(executionSemester);
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasDontWantToRespond() {
        return getDontWantToRespond() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasWeeklyHoursSpentInClassesSeason() {
        return getWeeklyHoursSpentInClassesSeason() != null;
    }

    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

}
