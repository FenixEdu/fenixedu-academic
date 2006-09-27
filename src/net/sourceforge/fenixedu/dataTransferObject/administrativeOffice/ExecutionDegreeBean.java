package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

public class ExecutionDegreeBean implements Serializable{

    private DomainReference<Degree> degree;
    private DomainReference<DegreeCurricularPlan> degreeCurricularPlan;
    private DomainReference<ExecutionDegree> executionDegree;
    
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
        this.degreeCurricularPlan = (degreeCurricularPlan != null) ? new DomainReference<DegreeCurricularPlan>(degreeCurricularPlan) : null; 
    }
    
    public ExecutionDegree getExecutionDegree() {
    	return (this.executionDegree == null) ? null : this.executionDegree.getObject();
    }
    
    public void setExecutionDegree(ExecutionDegree executionDegree) {
    	this.executionDegree = (executionDegree != null) ? new DomainReference<ExecutionDegree>(executionDegree) : null;
    }

}
