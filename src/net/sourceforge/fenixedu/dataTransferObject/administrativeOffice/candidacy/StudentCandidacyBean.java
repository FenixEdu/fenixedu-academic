package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

public abstract class StudentCandidacyBean implements Serializable {

	private Degree degree;
	private DegreeCurricularPlan degreeCurricularPlan;
	private ExecutionDegree executionDegree;

	public Degree getDegree() {
		return this.degree;
	}

	public void setDegree(Degree degree) {
		this.degree = degree;
	}

	public DegreeCurricularPlan getDegreeCurricularPlan() {
		return this.degreeCurricularPlan;
	}

	public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
		this.degreeCurricularPlan = degreeCurricularPlan;
	}

	public ExecutionDegree getExecutionDegree() {
		return this.executionDegree;
	}

	public void setExecutionDegree(ExecutionDegree executionDegree) {
		this.executionDegree = executionDegree;
	}

}
