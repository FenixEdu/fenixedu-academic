/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class ManageStudentStatuteBean implements Serializable {

	private StudentStatuteType statuteType;

	private ExecutionSemester beginExecutionPeriod;

	private ExecutionSemester executionPeriod;

	private ExecutionSemester endExecutionPeriod;

	private Student student;

	private Registration registration;

	public ManageStudentStatuteBean(Student student) {
		super();
		this.student = student;
		this.executionPeriod = ExecutionSemester.readActualExecutionSemester();
	}

	public ExecutionSemester getExecutionPeriod() {
		return executionPeriod;
	}

	public void setExecutionPeriod(ExecutionSemester executionSemester) {
		executionPeriod = executionSemester;
	}

	public ExecutionSemester getBeginExecutionPeriod() {
		return beginExecutionPeriod;
	}

	public ExecutionSemester getEndExecutionPeriod() {
		return endExecutionPeriod;
	}

	public StudentStatuteType getStatuteType() {
		return statuteType;
	}

	public Student getStudent() {
		return student;
	}

	public Registration getRegistration() {
		return registration;
	}

	public void setBeginExecutionPeriod(ExecutionSemester beginExecutionPeriod) {
		this.beginExecutionPeriod = beginExecutionPeriod;
	}

	public void setEndExecutionPeriod(ExecutionSemester endExecutionPeriod) {
		this.endExecutionPeriod = endExecutionPeriod;
	}

	public void setStatuteType(StudentStatuteType statuteType) {
		this.statuteType = statuteType;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public void setRegistration(Registration registration) {
		this.registration = registration;
	}

}
