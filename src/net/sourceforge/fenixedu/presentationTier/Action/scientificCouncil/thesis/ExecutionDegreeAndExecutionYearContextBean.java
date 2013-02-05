package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionYear;

public class ExecutionDegreeAndExecutionYearContextBean implements Serializable, HasExecutionYear {

    private static final long serialVersionUID = 1L;

    private ExecutionYear executionYear;
    private ExecutionDegree executionDegree;

    public ExecutionDegreeAndExecutionYearContextBean() {
    }

    public ExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    public void setExecutionDegree(final ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    @Override
    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

}
