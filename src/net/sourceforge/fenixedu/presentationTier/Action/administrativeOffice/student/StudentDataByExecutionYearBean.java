package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;

public class StudentDataByExecutionYearBean implements Serializable {

    private DomainReference<Student> student;
    private DomainReference<ExecutionYear> executionYear;
    private StudentPersonalDataAuthorizationChoice choice;

    public StudentDataByExecutionYearBean(final Student student) {
	setStudent(student);
    }

    public Student getStudent() {
	return (this.student != null) ? this.student.getObject() : null;
    }

    public void setStudent(Student student) {
	this.student = (student != null) ? new DomainReference<Student>(student) : null;
    }

    public ExecutionYear getExecutionYear() {
	return (this.executionYear != null) ? this.executionYear.getObject() : null;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = (executionYear != null) ? new DomainReference<ExecutionYear>(executionYear) : null;
    }

    public StudentPersonalDataAuthorizationChoice getChoice() {
	return choice;
    }

    public void setChoice(StudentPersonalDataAuthorizationChoice choice) {
	this.choice = choice;
    }

}
