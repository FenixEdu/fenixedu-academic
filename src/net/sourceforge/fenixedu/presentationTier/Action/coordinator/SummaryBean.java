package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

public class SummaryBean {

	private final ExecutionDegree executionDegree;
	private final DegreeCurricularPlan degreeCurricularPlan;

	public SummaryBean(ExecutionDegree executionDegree, DegreeCurricularPlan degreeCurricularPlan) {
		this.executionDegree = executionDegree;
		this.degreeCurricularPlan = degreeCurricularPlan;
	}

	public ExecutionDegree getExecutionDegree() {
		return executionDegree;
	}

	public DegreeCurricularPlan getDegreeCurricularPlan() {
		return degreeCurricularPlan;
	}
}
