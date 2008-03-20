package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class ThesisCreationPeriodContextBean implements Serializable {

    private DomainReference<ExecutionYear> executionYear;

    private DomainReference<ExecutionDegree> executionDegree;

    public ThesisCreationPeriodContextBean() {
    }

    public ExecutionYear getExecutionYear() {
	return executionYear == null ? null : executionYear.getObject();
    }

    public ExecutionDegree getExecutionDegree() {
	return executionDegree == null ? null : executionDegree.getObject();
    }

    public void setExecutionYear(final ExecutionYear executionYear) {
	this.executionYear = executionYear == null ? null : new DomainReference<ExecutionYear>(executionYear);
    }

    public void setExecutionDegree(final ExecutionDegree executionDegree) {
	this.executionDegree = executionDegree == null ? null : new DomainReference<ExecutionDegree>(executionDegree);
    }

}
