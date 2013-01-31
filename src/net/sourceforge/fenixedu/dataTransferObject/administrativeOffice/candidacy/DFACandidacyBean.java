package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

public class DFACandidacyBean implements Serializable {

	private DegreeType degreeType;

	private Degree degree;

	private DegreeCurricularPlan degreeCurricularPlan;

	private ExecutionDegree executionDegree;

	private ExecutionYear executionYear;

	public DFACandidacyBean() {
		degreeType = DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA;
	}

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
		return (this.executionYear == null || this.degreeCurricularPlan == null) ? null : getDegreeCurricularPlan()
				.getExecutionDegreeByYear(getExecutionYear());
	}

	public void setExecutionDegree(ExecutionDegree executionDegree) {
		this.executionDegree = executionDegree;
	}

	public ExecutionYear getExecutionYear() {
		return this.executionYear;
	}

	public void setExecutionYear(ExecutionYear executionYear) {
		this.executionYear = executionYear;
	}

	public DegreeType getDegreeType() {
		return degreeType;
	}

	public void setDegreeType(DegreeType degreeType) {
		this.degreeType = degreeType;
	}

}
