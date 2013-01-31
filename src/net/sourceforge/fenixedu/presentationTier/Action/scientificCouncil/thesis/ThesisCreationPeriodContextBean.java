package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class ThesisCreationPeriodContextBean implements Serializable {

	private ExecutionYear executionYear;

	private ExecutionDegree executionDegree;

	public ThesisCreationPeriodContextBean() {
	}

	public ExecutionYear getExecutionYear() {
		return executionYear;
	}

	public ExecutionDegree getExecutionDegree() {
		return executionDegree;
	}

	public void setExecutionYear(final ExecutionYear executionYear) {
		this.executionYear = executionYear;
	}

	public void setExecutionDegree(final ExecutionDegree executionDegree) {
		this.executionDegree = executionDegree;
	}

}
