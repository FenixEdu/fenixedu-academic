package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;

public abstract class TeachersByExecutionPeriodDegreeAndPlan extends LeafGroup {

    private static final long serialVersionUID = 1L;

    private final ExecutionSemester executionPeriod;
    private final Degree degree;
    private final DegreeCurricularPlan degreeCurricularPlan;

    public TeachersByExecutionPeriodDegreeAndPlan(ExecutionSemester executionPeriod, Degree degree, DegreeCurricularPlan plan) {
        super();
        this.degree = degree;
        this.executionPeriod = executionPeriod;
        this.degreeCurricularPlan = plan;
    }

    public ExecutionSemester getExecutionPeriod() {
        return executionPeriod;
    }

    public Degree getDegree() {
        return degree;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new IdOperator(getExecutionPeriod()), new IdOperator(getDegree()),
                new IdOperator(getDegreeCurricularPlan()) };
    }

}
