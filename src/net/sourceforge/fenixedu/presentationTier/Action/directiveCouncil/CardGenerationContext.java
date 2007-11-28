package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class CardGenerationContext implements Serializable {

    private DomainReference<ExecutionYear> executionYear;

    public CardGenerationContext() {
	executionYear = executionYearDR(ExecutionYear.readCurrentExecutionYear());
    }

    public CardGenerationContext(final ExecutionYear executionYear) {
	setExecutionYear(executionYear);
    }

    public ExecutionYear getExecutionYear() {
        return executionYear == null ? null : executionYear.getObject();
    }

    public void setExecutionYear(final ExecutionYear executionYear) {
        this.executionYear = executionYearDR(executionYear);
    }

    private DomainReference<ExecutionYear> executionYearDR(final ExecutionYear executionYear) {
	return new DomainReference<ExecutionYear>(executionYear);
    }

}
