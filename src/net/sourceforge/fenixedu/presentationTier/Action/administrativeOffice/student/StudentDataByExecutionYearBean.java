package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;

public class StudentDataByExecutionYearBean implements Serializable {

    private Student student;
    private ExecutionYear executionYear;
    private StudentPersonalDataAuthorizationChoice choice;

    public StudentDataByExecutionYearBean(final Student student) {
	setStudent(student);
    }

    public Student getStudent() {
	return this.student;
    }

    public void setStudent(Student student) {
	this.student = student;
    }

    public ExecutionYear getExecutionYear() {
	return this.executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = executionYear;
    }

    public StudentPersonalDataAuthorizationChoice getChoice() {
	return choice;
    }

    public void setChoice(StudentPersonalDataAuthorizationChoice choice) {
	this.choice = choice;
    }

}
