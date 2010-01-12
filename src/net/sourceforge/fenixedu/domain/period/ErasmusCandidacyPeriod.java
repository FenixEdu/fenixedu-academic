package net.sourceforge.fenixedu.domain.period;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class ErasmusCandidacyPeriod extends ErasmusCandidacyPeriod_Base {

    public ErasmusCandidacyPeriod() {
	super();
    }

    public ErasmusCandidacyPeriod(final ErasmusCandidacyProcess candidacyProcess, final ExecutionYear executionInterval,
	    final DateTime start, final DateTime end) {
	this();
	init(candidacyProcess, executionInterval, start, end);
    }

    private void init(final ErasmusCandidacyProcess candidacyProcess, final ExecutionInterval executionInterval,
	    final DateTime start, final DateTime end) {
	checkParameters(candidacyProcess);
	checkIfCanCreate(executionInterval, start, end);
	super.init(executionInterval, start, end);
	addCandidacyProcesses(candidacyProcess);
    }

    private void checkParameters(final ErasmusCandidacyProcess candidacyProcess) {
	if (candidacyProcess == null) {
	    throw new DomainException("error.ErasmusCandidacyProcess.invalid.candidacy.process");
	}
    }

    private void checkIfCanCreate(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
	for (final ErasmusCandidacyPeriod erasmusCandidacyPeriod : executionInterval.getErasmusCandidacyPeriods()) {
	    if (erasmusCandidacyPeriod.intercept(start, end)) {
		throw new DomainException("error.ErasmusCandidacyPeriod.interception", executionInterval.getName(), start
			.toString("dd/MM/yyyy HH:mm"), end.toString("dd/MM/yyyy HH:mm"));
	    }
	}
    }

    public ErasmusCandidacyProcess getErasmusCandidacyProcess() {
	return (ErasmusCandidacyProcess) (hasAnyCandidacyProcesses() ? getCandidacyProcesses().get(0) : null);
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
	checkIfCanEdit(start, end);
	super.setStart(start);
	super.setEnd(end);
    }

    private void checkIfCanEdit(DateTime start, DateTime end) {
	for (final ErasmusCandidacyPeriod erasmusCandidacyPeriod : getExecutionInterval().getErasmusCandidacyPeriods()) {
	    if (erasmusCandidacyPeriod != this && erasmusCandidacyPeriod.intercept(start, end)) {
		throw new DomainException("error.ErasmusCandidacyPeriod.interception", getExecutionInterval().getName(), start
			.toString("dd/MM/yyyy HH:mm"), end.toString("dd/MM/yyyy HH:mm"));
	    }
	}
    }

}
