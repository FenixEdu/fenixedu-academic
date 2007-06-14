package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class DFACandidacyBean implements Serializable {

    private DomainReference<Degree> degree;

    private DomainReference<DegreeCurricularPlan> degreeCurricularPlan;

    private DomainReference<ExecutionDegree> executionDegree;

    private DomainReference<ExecutionYear> executionYear;

    public Degree getDegree() {
	return (this.degree == null) ? null : this.degree.getObject();
    }

    public void setDegree(Degree degree) {
	this.degree = (degree != null) ? new DomainReference<Degree>(degree) : null;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
	return (this.degreeCurricularPlan == null) ? null : this.degreeCurricularPlan.getObject();
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
	this.degreeCurricularPlan = (degreeCurricularPlan != null) ? new DomainReference<DegreeCurricularPlan>(
		degreeCurricularPlan)
		: null;
    }

    public ExecutionDegree getExecutionDegree() {
	return (this.executionYear == null || this.degreeCurricularPlan == null) ? null
		: getDegreeCurricularPlan().getExecutionDegreeByYear(getExecutionYear());
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
	this.executionDegree = (executionDegree != null) ? new DomainReference<ExecutionDegree>(
		executionDegree) : null;
    }

    public ExecutionYear getExecutionYear() {
	return (this.executionYear == null) ? null : this.executionYear.getObject();
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = (executionYear != null) ? new DomainReference<ExecutionYear>(executionYear)
		: null;
    }

}
