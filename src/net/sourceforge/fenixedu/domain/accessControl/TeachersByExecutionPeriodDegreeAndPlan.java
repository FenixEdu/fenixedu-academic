package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;

public abstract class TeachersByExecutionPeriodDegreeAndPlan extends LeafGroup {

    private static final long serialVersionUID = 1L;

    private DomainReference<ExecutionSemester> executionPeriod;
    private DomainReference<Degree> degree;
    private DomainReference<DegreeCurricularPlan> degreeCurricularPlan;

    public TeachersByExecutionPeriodDegreeAndPlan(ExecutionSemester executionPeriod, Degree degree, DegreeCurricularPlan plan) {
	super();
	setDegree(degree);
	setExecutionPeriod(executionPeriod);
	setDegreeCurricularPlan(plan);
    }

    public ExecutionSemester getExecutionPeriod() {
	return executionPeriod == null ? null : executionPeriod.getObject();
    }

    public Degree getDegree() {
	return degree == null ? null : degree.getObject();
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
	return degreeCurricularPlan == null ? null : degreeCurricularPlan.getObject();
    }

    public void setExecutionPeriod(ExecutionSemester executionPeriod) {
	this.executionPeriod = new DomainReference<ExecutionSemester>(executionPeriod);
    }

    public void setDegree(Degree degree) {
	this.degree = new DomainReference<Degree>(degree);
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
	this.degreeCurricularPlan = new DomainReference<DegreeCurricularPlan>(degreeCurricularPlan);
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] { new IdOperator(getExecutionPeriod()), new IdOperator(getDegree()),
		new IdOperator(getDegreeCurricularPlan()) };
    }

}
