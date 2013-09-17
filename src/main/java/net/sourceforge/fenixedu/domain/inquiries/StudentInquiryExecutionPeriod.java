package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Student;

public class StudentInquiryExecutionPeriod extends StudentInquiryExecutionPeriod_Base {

    public StudentInquiryExecutionPeriod() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public StudentInquiryExecutionPeriod(Student student, ExecutionSemester executionSemester) {
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
