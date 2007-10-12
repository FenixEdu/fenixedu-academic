package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;

public class ExecutionDegreeBean implements Serializable {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    private DomainReference<Degree> degree;

    private DomainReference<DegreeCurricularPlan> degreeCurricularPlan;

    private DomainReference<ExecutionDegree> executionDegree;

    private DomainReference<ExecutionYear> executionYear;

    private CycleType cycleType;

    public ExecutionDegreeBean() {
	super();

	this.degree = new DomainReference<Degree>(null);
	this.degreeCurricularPlan = new DomainReference<DegreeCurricularPlan>(null);
	this.executionDegree = new DomainReference<ExecutionDegree>(null);
	this.executionYear = new DomainReference<ExecutionYear>(null);
    }

    public ExecutionDegreeBean(Degree degree) {
	this();

	setDegree(degree);
    }

    public ExecutionDegreeBean(DegreeCurricularPlan degreeCurricularPlan) {
	this(degreeCurricularPlan == null ? null : degreeCurricularPlan.getDegree());

	setDegreeCurricularPlan(degreeCurricularPlan);
    }

    public ExecutionDegreeBean(ExecutionDegree executionDegree) {
	this(executionDegree == null ? null : executionDegree.getDegreeCurricularPlan());

	setExecutionDegree(executionDegree);
    }

    public Degree getDegree() {
	return this.degree.getObject();
    }

    public void setDegree(Degree degree) {
	this.degree = new DomainReference<Degree>(degree);
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
	return this.degreeCurricularPlan.getObject();
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
	this.degreeCurricularPlan = new DomainReference<DegreeCurricularPlan>(degreeCurricularPlan);
    }

    public ExecutionDegree getExecutionDegree() {
	return this.executionDegree.getObject();
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
	this.executionDegree = new DomainReference<ExecutionDegree>(executionDegree);
    }

    public ExecutionYear getExecutionYear() {
	return this.executionYear.getObject();
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = new DomainReference<ExecutionYear>(executionYear);
    }

    public CycleType getCycleType() {
	return cycleType;
    }

    public void setCycleType(CycleType cycleType) {
	this.cycleType = cycleType;
    }

}
