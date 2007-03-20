package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class ThesisContextBean implements Serializable{

    /**
     * Serial version id. 
     */
    private static final long serialVersionUID = 1L;
    
    private DomainReference<Degree> degree;
    private DomainReference<ExecutionYear> executionYear;
    
    public ThesisContextBean (Degree degree, ExecutionYear executionYear) {
	setDegree(degree);
	setExecutionYear(executionYear);
    }

    public Degree getDegree() {
        return this.degree.getObject();
    }

    public void setDegree(Degree degree) {
        this.degree = new DomainReference<Degree>(degree);
    }

    public ExecutionYear getExecutionYear() {
        return this.executionYear.getObject();
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = new DomainReference<ExecutionYear>(executionYear);
    }

}
