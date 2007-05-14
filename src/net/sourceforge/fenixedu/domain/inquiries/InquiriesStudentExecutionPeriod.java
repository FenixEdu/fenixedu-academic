package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Student;

public class InquiriesStudentExecutionPeriod extends InquiriesStudentExecutionPeriod_Base {
    
    public InquiriesStudentExecutionPeriod() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public InquiriesStudentExecutionPeriod(final Student student, final ExecutionPeriod executionPeriod) {
	this();
	setStudent(student);
	setExecutionPeriod(executionPeriod);
    }
    
}
