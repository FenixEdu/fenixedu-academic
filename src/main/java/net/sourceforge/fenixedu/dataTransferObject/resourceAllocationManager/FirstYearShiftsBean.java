package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionYear;

public class FirstYearShiftsBean implements Serializable {
    private ExecutionYear executionYear;
    private EntryPhase phase;

    public FirstYearShiftsBean() {
        this.executionYear = ExecutionYear.readCurrentExecutionYear();
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear academicInterval) {
        this.executionYear = academicInterval;
    }

    public EntryPhase getEntryPhase() {
        return phase;
    }

    public void setEntryPhase(EntryPhase phase) {
        this.phase = phase;
    }

}