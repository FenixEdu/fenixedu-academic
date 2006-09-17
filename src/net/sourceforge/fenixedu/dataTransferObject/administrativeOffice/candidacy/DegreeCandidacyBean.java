package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class DegreeCandidacyBean extends StudentCandidacyBean {

	private DomainReference<ExecutionYear> executionYear;
	
	public ExecutionYear getExecutionYear() {
        return (this.executionYear == null) ? null : this.executionYear.getObject();
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = (executionYear != null) ? new DomainReference<ExecutionYear>(executionYear) : null; 
    }
}