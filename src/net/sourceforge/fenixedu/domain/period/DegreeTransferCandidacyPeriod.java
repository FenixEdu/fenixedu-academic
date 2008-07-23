package net.sourceforge.fenixedu.domain.period;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferCandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class DegreeTransferCandidacyPeriod extends DegreeTransferCandidacyPeriod_Base {

    private DegreeTransferCandidacyPeriod() {
	super();
    }

    public DegreeTransferCandidacyPeriod(final DegreeTransferCandidacyProcess process, final ExecutionYear executionYear,
	    final DateTime start, final DateTime end) {
	this();
	checkParameters(process);
	checkIfCanCreate(executionYear);
	super.init(executionYear, start, end);
	addCandidacyProcesses(process);
    }

    private void checkParameters(final DegreeTransferCandidacyProcess candidacyProcess) {
	if (candidacyProcess == null) {
	    throw new DomainException("error.DegreeTransferCandidacyPeriod.invalid.candidacy.process");
	}
    }

    private void checkIfCanCreate(final ExecutionInterval executionInterval) {
	if (executionInterval.hasDegreeTransferCandidacyPeriod()) {
	    throw new DomainException(
		    "error.DegreeTransferCandidacyPeriod.executionInterval.already.contains.candidacyPeriod.type",
		    executionInterval.getName());
	}
    }

    @Override
    public ExecutionYear getExecutionInterval() {
	return (ExecutionYear) super.getExecutionInterval();
    }

    public DegreeTransferCandidacyProcess getDegreeTransferCandidacyProcess() {
	return (DegreeTransferCandidacyProcess) (hasAnyCandidacyProcesses() ? getCandidacyProcesses().get(0) : null);
    }
}
