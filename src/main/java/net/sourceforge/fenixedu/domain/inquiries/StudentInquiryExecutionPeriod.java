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
}
