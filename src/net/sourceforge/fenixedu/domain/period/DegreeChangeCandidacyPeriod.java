package net.sourceforge.fenixedu.domain.period;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange.DegreeChangeCandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class DegreeChangeCandidacyPeriod extends DegreeChangeCandidacyPeriod_Base {

    private DegreeChangeCandidacyPeriod() {
	super();
    }

    public DegreeChangeCandidacyPeriod(final DegreeChangeCandidacyProcess candidacyProcess, final ExecutionYear executionYear,
	    final DateTime start, final DateTime end) {
	this();
	checkParameters(candidacyProcess);
	checkIfCanCreate(executionYear);
	super.init(executionYear, start, end);
	addCandidacyProcesses(candidacyProcess);
    }

    private void checkIfCanCreate(final ExecutionInterval executionInterval) {
	if (executionInterval.hasDegreeChangeCandidacyPeriod()) {
	    throw new DomainException(
		    "error.DegreeChangeCandidacyPeriod.executionInterval.already.contains.candidacyPeriod.type",
		    executionInterval.getName());
	}
    }

    private void checkParameters(final DegreeChangeCandidacyProcess candidacyProcess) {
	if (candidacyProcess == null) {
	    throw new DomainException("error.DegreeChangeCandidacyPeriod.invalid.candidacy.process");
	}
    }

    public DegreeChangeCandidacyProcess getDegreeChangeCandidacyProcess() {
	return (DegreeChangeCandidacyProcess) (hasAnyCandidacyProcesses() ? getCandidacyProcesses().get(0) : null);
    }

    @Override
    public ExecutionYear getExecutionInterval() {
	return (ExecutionYear) super.getExecutionInterval();
    }
}
