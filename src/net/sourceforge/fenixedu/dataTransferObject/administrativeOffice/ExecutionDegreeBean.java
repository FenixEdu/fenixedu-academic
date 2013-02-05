package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;

public class ExecutionDegreeBean implements Serializable {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    private Degree degree;

    private DegreeCurricularPlan degreeCurricularPlan;

    private ExecutionDegree executionDegree;

    private ExecutionYear executionYear;

    private CycleType cycleType;

    public ExecutionDegreeBean() {
        super();

        this.degree = null;
        this.degreeCurricularPlan = null;
        this.executionDegree = null;
        this.executionYear = null;
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

    public ExecutionYear getExecutionYear() {
        return this.executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public CycleType getCycleType() {
        return cycleType;
    }

    public void setCycleType(CycleType cycleType) {
        this.cycleType = cycleType;
    }

}
