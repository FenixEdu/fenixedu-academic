package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class ExecutionCourseAudit extends ExecutionCourseAudit_Base {

    public ExecutionCourseAudit(ExecutionCourse executionCourse) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setExecutionCourse(executionCourse);
    }

    public void edit(Person teacher, Person student) {
	if (!student.hasRole(RoleType.STUDENT)) {
	    throw new DomainException("error.inquiry.audit.hasToBeStudent");
	}
	if (!teacher.hasRole(RoleType.TEACHER)) {
	    throw new DomainException("error.inquiry.audit.hasToBeTeacher");
	}
	setTeacherAuditor(teacher.getTeacher());
	setStudentAuditor(student.getStudent());
    }
}
