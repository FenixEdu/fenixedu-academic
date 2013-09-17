package net.sourceforge.fenixedu.domain.period;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23CandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class Over23CandidacyPeriod extends Over23CandidacyPeriod_Base {

    private Over23CandidacyPeriod() {
        super();
    }

    public Over23CandidacyPeriod(final Over23CandidacyProcess candidacyProcess, final ExecutionYear executionYear,
            final DateTime start, final DateTime end) {
        this();
        init(candidacyProcess, executionYear, start, end);
    }

    private void init(final Over23CandidacyProcess candidacyProcess, final ExecutionInterval executionInterval,
            final DateTime start, final DateTime end) {
        String[] args = {};
        if (candidacyProcess == null) {
            throw new DomainException("error.Over23CandidacyPeriod.invalid.candidacy.process", args);
        }
        checkIfCanCreate(executionInterval);
        super.init(executionInterval, start, end);
        addCandidacyProcesses(candidacyProcess);
    }

    private void checkIfCanCreate(final ExecutionInterval executionInterval) {
        if (executionInterval.hasOver23CandidacyPeriod()) {
            throw new DomainException("error.Over23CandidacyPeriod.executionInterval.already.contains.candidacyPeriod.type",
                    executionInterval.getName());
        }
    }

    public Over23CandidacyProcess getOver23CandidacyProcess() {
        return (Over23CandidacyProcess) (hasAnyCandidacyProcesses() ? getCandidacyProcesses().iterator().next() : null);
    }

    @Override
    public ExecutionYear getExecutionInterval() {
        return (ExecutionYear) super.getExecutionInterval();
    }
}
