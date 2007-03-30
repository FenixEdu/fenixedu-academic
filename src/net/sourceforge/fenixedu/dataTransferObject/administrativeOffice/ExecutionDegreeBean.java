package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

public class ExecutionDegreeBean implements Serializable{

    /**
     * Serial version id. 
     */
    private static final long serialVersionUID = 1L;
    
    private DomainReference<Degree> degree;
    private DomainReference<DegreeCurricularPlan> degreeCurricularPlan;
    private DomainReference<ExecutionDegree> executionDegree;
    
    public ExecutionDegreeBean() {
        super();

        this.degree = new DomainReference<Degree>(null);
        this.degreeCurricularPlan = new DomainReference<DegreeCurricularPlan>(null);
        this.executionDegree = new DomainReference<ExecutionDegree>(null);
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

}
