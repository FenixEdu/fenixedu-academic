package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;

public abstract class TeachersByExecutionPeriodDegreeAndPlan extends LeafGroup {

    private static final long serialVersionUID = 1L;

    private final DomainReference<ExecutionSemester> executionPeriod;
    private final DomainReference<Degree> degree;
    private final DomainReference<DegreeCurricularPlan> degreeCurricularPlan;

    public TeachersByExecutionPeriodDegreeAndPlan(ExecutionSemester executionPeriod, Degree degree, DegreeCurricularPlan plan) {
	super();
	this.degree = new DomainReference<Degree>(degree);
	this.executionPeriod = new DomainReference<ExecutionSemester>(executionPeriod);
	this.degreeCurricularPlan = new DomainReference<DegreeCurricularPlan>(plan);
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

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] { new IdOperator(getExecutionPeriod()), new IdOperator(getDegree()),
		new IdOperator(getDegreeCurricularPlan()) };
    }

}
