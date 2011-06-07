package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.inquiries.ExecutionCourseAudit;
import pt.ist.fenixWebFramework.services.Service;

public class AuditSelectPersonsECBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ExecutionCourse executionCourse;
    private Person student;
    private Person teacher;

    public AuditSelectPersonsECBean(ExecutionCourse executionCourse) {
	setExecutionCourse(executionCourse);
    }

    public AuditSelectPersonsECBean(ExecutionCourseAudit executionCourseAudit) {
	setExecutionCourse(executionCourseAudit.getExecutionCourse());
	setStudent(executionCourseAudit.getStudentAuditor().getPerson());
	setTeacher(executionCourseAudit.getTeacherAuditor().getPerson());
    }

    public Person getStudent() {
	return student;
    }

    public void setStudent(Person student) {
	this.student = student;
    }

    public Person getTeacher() {
	return teacher;
    }

    public void setTeacher(Person teacher) {
	this.teacher = teacher;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
	this.executionCourse = executionCourse;
    }

    public ExecutionCourse getExecutionCourse() {
	return executionCourse;
    }

    @Service
    public void savePersons() {
	ExecutionCourseAudit executionCourseAudit = getExecutionCourse().getExecutionCourseAudit();
	if (executionCourseAudit == null) {
	    executionCourseAudit = new ExecutionCourseAudit(getExecutionCourse());
	}
	executionCourseAudit.edit(getTeacher(), getStudent());
    }
}
