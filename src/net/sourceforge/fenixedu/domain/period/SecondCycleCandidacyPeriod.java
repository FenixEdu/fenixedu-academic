package net.sourceforge.fenixedu.domain.period;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class SecondCycleCandidacyPeriod extends SecondCycleCandidacyPeriod_Base {

    private SecondCycleCandidacyPeriod() {
	super();
    }

    public SecondCycleCandidacyPeriod(final SecondCycleCandidacyProcess candidacyProcess, final ExecutionYear executionInterval,
	    final DateTime start, final DateTime end) {
	this();
	init(candidacyProcess, executionInterval, start, end);
    }

    private void init(final SecondCycleCandidacyProcess candidacyProcess, final ExecutionInterval executionInterval,
	    final DateTime start, final DateTime end) {
	checkParameters(candidacyProcess);
	checkIfCanCreate(executionInterval, start, end);
	super.init(executionInterval, start, end);
	addCandidacyProcesses(candidacyProcess);
    }

    private void checkParameters(final SecondCycleCandidacyProcess candidacyProcess) {
	if (candidacyProcess == null) {
	    throw new DomainException("error.SecondCycleCandidacyPeriod.invalid.candidacy.process");
	}
    }

    private void checkIfCanCreate(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
	for (final SecondCycleCandidacyPeriod secondCycleCandidacyPeriod : executionInterval.getSecondCycleCandidacyPeriods()) {
	    if (secondCycleCandidacyPeriod.intercept(start, end)) {
		throw new DomainException("error.SecondCycleCandidacyPeriod.interception", executionInterval.getName(), start
			.toString("dd/MM/yyyy HH:mm"), end.toString("dd/MM/yyyy HH:mm"));
	    }
	}
    }

    private boolean intercept(final DateTime start, final DateTime end) {
	assert start.isBefore(end);
	return contains(start) || contains(end);
    }

    public SecondCycleCandidacyProcess getSecondCycleCandidacyProcess() {
	return (SecondCycleCandidacyProcess) (hasAnyCandidacyProcesses() ? getCandidacyProcesses().get(0) : null);
    }

    @Override
    public ExecutionYear getExecutionInterval() {
	return (ExecutionYear) super.getExecutionInterval();
    }

    public String getPresentationName() {
	return getStart().toString("dd/MM/yyyy") + " - " + getEnd().toString("dd/MM/yyyy");
    }

    @Override
    public void edit(final DateTime start, final DateTime end) {
	checkDates(start, end);
	checkIfCandEdit(start, end);
	super.setStart(start);
	super.setEnd(end);
    }

    private void checkIfCandEdit(DateTime start, DateTime end) {
	for (final SecondCycleCandidacyPeriod secondCycleCandidacyPeriod : getExecutionInterval()
		.getSecondCycleCandidacyPeriods()) {
	    if (secondCycleCandidacyPeriod != this && secondCycleCandidacyPeriod.intercept(start, end)) {
		throw new DomainException("error.SecondCycleCandidacyPeriod.interception", getExecutionInterval().getName(),
			start.toString("dd/MM/yyyy HH:mm"), end.toString("dd/MM/yyyy HH:mm"));
	    }
	}
    }
}
