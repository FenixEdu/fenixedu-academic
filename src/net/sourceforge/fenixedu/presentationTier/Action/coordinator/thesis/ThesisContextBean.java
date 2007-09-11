package net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis;

import java.io.Serializable;
import java.util.List;
import java.util.SortedSet;

import net.sourceforge.fenixedu.domain.DomainListReference;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class ThesisContextBean implements Serializable{

    /**
     * Serial version id. 
     */
    private static final long serialVersionUID = 1L;
    
    private DomainReference<ExecutionYear> selected;
    private DomainListReference<ExecutionYear> executionYears;
    
    public ThesisContextBean (SortedSet<ExecutionYear> executionYears, ExecutionYear selected) {
        setExecutionYearPossibilities(executionYears);
        setExecutionYear(selected);
    }

    public List<ExecutionYear> getExecutionYearPossibilities() {
        return this.executionYears;
    }
    
    public void setExecutionYearPossibilities(SortedSet<ExecutionYear> executionYears) {
        this.executionYears = new DomainListReference<ExecutionYear>(executionYears);
    }

    public ExecutionYear getExecutionYear() {
        return this.selected.getObject();
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.selected = new DomainReference<ExecutionYear>(executionYear);
    }

}
