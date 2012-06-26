package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.ist.fenixWebFramework.services.Service;

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

    @Service
    public void sealProcess(boolean isTeacher) {
	if (isTeacher) {
	    setApprovedByTeacher(true);
	} else {
	    setApprovedByStudent(true);
	}
    }

    @Service
    public void unsealProcess(boolean isTeacher) {
	if (isTeacher) {
	    setApprovedByTeacher(false);
	} else {
	    setApprovedByStudent(false);
	}
    }

    @Service
    public void unsealProcess() {
	setApprovedByTeacher(false);
	setApprovedByStudent(false);
    }

    @Service
    public void addFile(String filename, byte[] file) {
	new ExecutionCourseAuditFile(this, filename, file);
    }

    @Service
    public void deleteFile(ExecutionCourseAuditFile executionCourseAuditFile) {
	executionCourseAuditFile.delete();
    }

    @Service
    public void makeProcessAvailableToView() {
	setAvailableProcess(true);
    }

    @Service
    public void makeProcessUnavailableToView() {
	setAvailableProcess(false);
    }

    public boolean isProcessAvailable() {
	if (getAvailableProcess() == null) {
	    return false;
	}
	return getAvailableProcess();
    }
}
