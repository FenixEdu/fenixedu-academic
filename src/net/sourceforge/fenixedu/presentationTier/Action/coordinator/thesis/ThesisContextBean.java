package net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class ThesisContextBean implements Serializable{

    /**
     * Serial version id. 
     */
    private static final long serialVersionUID = 1L;
    
    private DomainReference<ExecutionYear> executionYear;
    
    public ThesisContextBean (ExecutionYear executionYear) {
	setExecutionYear(executionYear);
    }

    public ExecutionYear getExecutionYear() {
        return this.executionYear.getObject();
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = new DomainReference<ExecutionYear>(executionYear);
    }

}
