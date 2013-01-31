package net.sourceforge.fenixedu.dataTransferObject.research.result;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionYear;

public class ExecutionYearBean implements Serializable {

	private ExecutionYear executionYear;

	public ExecutionYearBean() {
		setExecutionYear(null);
	}

	public ExecutionYearBean(ExecutionYear executionYear) {
		setExecutionYear(executionYear);
	}

	public void setExecutionYear(ExecutionYear executionYear) {
		this.executionYear = executionYear;
	}

	public ExecutionYear getExecutionYear() {
		return this.executionYear;
	}
}
