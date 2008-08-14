package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionYear;

public class ExecutionDegreeAndExecutionYearContextBean implements Serializable, HasExecutionYear {

    private static final long serialVersionUID = 1L;

    private DomainReference<ExecutionYear> executionYear;
    private DomainReference<ExecutionDegree> executionDegree;

    public ExecutionDegreeAndExecutionYearContextBean() {
    }

    public ExecutionDegree getExecutionDegree() {
	return executionDegree == null ? null : executionDegree.getObject();
    }

    public void setExecutionDegree(final ExecutionDegree executionDegree) {
	this.executionDegree = executionDegree == null ? null : new DomainReference<ExecutionDegree>(executionDegree);
    }

    public ExecutionYear getExecutionYear() {
	return executionYear == null ? null : executionYear.getObject();
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = executionYear == null ? null : new DomainReference<ExecutionYear>(executionYear);
    }

}
