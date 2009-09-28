package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

public class SummaryBean {

    private final DomainReference<ExecutionDegree> executionDegree;
    private final DomainReference<DegreeCurricularPlan> degreeCurricularPlan;

    public SummaryBean(ExecutionDegree executionDegree, DegreeCurricularPlan degreeCurricularPlan) {
	this.executionDegree = new DomainReference<ExecutionDegree>(executionDegree);
	this.degreeCurricularPlan = new DomainReference<DegreeCurricularPlan>(degreeCurricularPlan);
    }

    public ExecutionDegree getExecutionDegree() {
	return executionDegree.getObject();
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
	return degreeCurricularPlan.getObject();
    }
}
