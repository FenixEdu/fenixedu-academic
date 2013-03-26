package net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis;

import java.io.Serializable;
import java.util.SortedSet;

import net.sourceforge.fenixedu.domain.ExecutionYear;

public class ThesisContextBean implements Serializable {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    private ExecutionYear selected;
    private SortedSet<ExecutionYear> executionYears;
    private ThesisPresentationState presentationState;

    public ThesisContextBean(SortedSet<ExecutionYear> executionYears, ExecutionYear selected) {
        setExecutionYearPossibilities(executionYears);
        setExecutionYear(selected);
    }

    public SortedSet<ExecutionYear> getExecutionYearPossibilities() {
        return this.executionYears;
    }

    public void setExecutionYearPossibilities(SortedSet<ExecutionYear> executionYears) {
        this.executionYears = executionYears;
    }

    public ExecutionYear getExecutionYear() {
        return this.selected;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.selected = executionYear;
    }

    public void setPresentationState(ThesisPresentationState presentationState) {
        this.presentationState = presentationState;
    }

    public ThesisPresentationState getPresentationState() {
        return presentationState;
    }

}
