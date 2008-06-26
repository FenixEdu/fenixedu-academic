package net.sourceforge.fenixedu.domain.period;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class SecondCycleCandidacyPeriod extends SecondCycleCandidacyPeriod_Base {

    private SecondCycleCandidacyPeriod() {
	super();
    }

    public SecondCycleCandidacyPeriod(final SecondCycleCandidacyProcess candidacyProcess,
	    final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
	this();
	init(candidacyProcess, executionInterval, start, end);
    }

    private void init(final SecondCycleCandidacyProcess candidacyProcess, final ExecutionInterval executionInterval,
	    final DateTime start, final DateTime end) {
	checkParameters(candidacyProcess);
	checkIfCanCreate(executionInterval);
	super.init(executionInterval, start, end);
	addCandidacyProcesses(candidacyProcess);
    }

    private void checkParameters(final SecondCycleCandidacyProcess candidacyProcess) {
	if (candidacyProcess == null) {
	    throw new DomainException("error.SecondCycleCandidacyPeriod.invalid.candidacy.process");
	}
    }

    private void checkIfCanCreate(final ExecutionInterval executionInterval) {
	if (executionInterval.hasSecondCycleCandidacyPeriod()) {
	    throw new DomainException("error.SecondCycleCandidacyPeriod.executionInterval.already.contains.candidacyPeriod.type",
		    executionInterval.getName());
	}
    }

    public SecondCycleCandidacyProcess getSecondCycleCandidacyProcess() {
	return (SecondCycleCandidacyProcess) (hasAnyCandidacyProcesses() ? getCandidacyProcesses().get(0) : null);
    }
}
