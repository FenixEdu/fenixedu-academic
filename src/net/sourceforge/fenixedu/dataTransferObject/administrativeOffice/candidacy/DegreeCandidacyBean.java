package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy;

import net.sourceforge.fenixedu.domain.ExecutionYear;

public class DegreeCandidacyBean extends StudentCandidacyBean {

	private ExecutionYear executionYear;

	public ExecutionYear getExecutionYear() {
		return this.executionYear;
	}

	public void setExecutionYear(ExecutionYear executionYear) {
		this.executionYear = executionYear;
	}
}