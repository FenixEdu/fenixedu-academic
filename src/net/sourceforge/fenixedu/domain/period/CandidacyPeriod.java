package net.sourceforge.fenixedu.domain.period;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

abstract public class CandidacyPeriod extends CandidacyPeriod_Base {

    protected CandidacyPeriod() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    protected void init(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
	checkParameters(executionInterval, start, end);
	setExecutionInterval(executionInterval);
	setStart(start);
	setEnd(end);
    }

    protected void checkParameters(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
	if (executionInterval == null) {
	    throw new DomainException("error.CandidacyPeriod.invalid.academic.period");
	}
	checkDates(start, end);
    }

    protected void checkDates(final DateTime start, final DateTime end) {
	if (start == null || end == null || start.isAfter(end)) {
	    throw new DomainException("error.CandidacyPeriod.invalid.dates");
	}
    }

    public void edit(final DateTime start, final DateTime end) {
	checkDates(start, end);
	super.setStart(start);
	super.setEnd(end);
    }

    public boolean isOpen() {
	return contains(new DateTime());
    }

    public boolean isOpen(final DateTime date) {
	return contains(date);
    }

    public boolean contains(final DateTime date) {
	return !(getStart().isAfter(date) || getEnd().isBefore(date));
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
}
