package net.sourceforge.fenixedu.domain.period;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;

abstract public class CandidacyProcessCandidacyPeriod extends CandidacyProcessCandidacyPeriod_Base {

    protected CandidacyProcessCandidacyPeriod() {
        super();
    }

    public boolean hasCandidacyProcesses(final Class<? extends CandidacyProcess> clazz, final ExecutionInterval executionInterval) {
        return hasExecutionInterval(executionInterval) && containsCandidacyProcess(clazz);
    }

    public boolean hasExecutionInterval(final ExecutionInterval executionInterval) {
        return getExecutionInterval() == executionInterval;
    }

    public boolean containsCandidacyProcess(final Class<? extends CandidacyProcess> clazz) {
        for (final CandidacyProcess process : getCandidacyProcesses()) {
            if (process.getClass().equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    public List<CandidacyProcess> getCandidacyProcesses(final Class<? extends CandidacyProcess> clazz) {
        final List<CandidacyProcess> result = new ArrayList<CandidacyProcess>();
        for (final CandidacyProcess process : getCandidacyProcesses()) {
            if (process.getClass().equals(clazz)) {
                result.add(process);
            }
        }
        return result;
    }
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess> getCandidacyProcesses() {
        return getCandidacyProcessesSet();
    }

    @Deprecated
    public boolean hasAnyCandidacyProcesses() {
        return !getCandidacyProcessesSet().isEmpty();
    }

}
