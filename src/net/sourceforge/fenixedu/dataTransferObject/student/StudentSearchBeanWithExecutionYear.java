package net.sourceforge.fenixedu.dataTransferObject.student;

import net.sourceforge.fenixedu.domain.ExecutionYear;

public class StudentSearchBeanWithExecutionYear extends StudentsSearchBean {

	private ExecutionYear executionYearDomainReference;

	public ExecutionYear getExecutionYear() {
		return executionYearDomainReference;
	}

	public void setExecutionYear(ExecutionYear executionYear) {
		this.executionYearDomainReference = executionYear;
	}

}
