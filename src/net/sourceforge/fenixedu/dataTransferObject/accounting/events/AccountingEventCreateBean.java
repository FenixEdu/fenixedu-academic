package net.sourceforge.fenixedu.dataTransferObject.accounting.events;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class AccountingEventCreateBean implements Serializable {

	static private final long serialVersionUID = 5068902775407522276L;

	private StudentCurricularPlan studentCurricularPlan;

	private ExecutionYear executionYear;

	private ExecutionSemester executionSemester;

	public AccountingEventCreateBean() {
	}

	public AccountingEventCreateBean(final StudentCurricularPlan studentCurricularPlan) {
		setStudentCurricularPlan(studentCurricularPlan);
	}

	public StudentCurricularPlan getStudentCurricularPlan() {
		return this.studentCurricularPlan;
	}

	public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
		this.studentCurricularPlan = studentCurricularPlan;
	}

	public ExecutionYear getExecutionYear() {
		return this.executionYear;
	}

	public void setExecutionYear(ExecutionYear executionYear) {
		this.executionYear = executionYear;
	}

	public ExecutionSemester getExecutionPeriod() {
		return this.executionSemester;
	}

	public void setExecutionPeriod(ExecutionSemester executionSemester) {
		this.executionSemester = executionSemester;
	}

}
