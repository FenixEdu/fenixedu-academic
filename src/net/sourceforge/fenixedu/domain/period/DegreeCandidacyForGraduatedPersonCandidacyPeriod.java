package net.sourceforge.fenixedu.domain.period;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class DegreeCandidacyForGraduatedPersonCandidacyPeriod extends DegreeCandidacyForGraduatedPersonCandidacyPeriod_Base {

    private DegreeCandidacyForGraduatedPersonCandidacyPeriod() {
	super();
    }

    public DegreeCandidacyForGraduatedPersonCandidacyPeriod(final DegreeCandidacyForGraduatedPersonProcess candidacyProcess,
	    final ExecutionYear executionYear, final DateTime start, final DateTime end) {
	this();
	init(candidacyProcess, executionYear, start, end);
    }

    private void init(final DegreeCandidacyForGraduatedPersonProcess candidacyProcess, final ExecutionInterval executionInterval,
	    final DateTime start, final DateTime end) {
	checkParameters(candidacyProcess);
	checkIfCanCreate(executionInterval);
	super.init(executionInterval, start, end);
	addCandidacyProcesses(candidacyProcess);
    }

    private void checkParameters(final DegreeCandidacyForGraduatedPersonProcess candidacyProcess) {
	if (candidacyProcess == null) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPersonCandidacyPeriod.invalid.candidacy.process");
	}
    }

    private void checkIfCanCreate(final ExecutionInterval executionInterval) {
	if (executionInterval.hasDegreeCandidacyForGraduatedPersonCandidacyPeriod()) {
	    throw new DomainException(
		    "error.DegreeCandidacyForGraduatedPersonCandidacyPeriod.executionInterval.already.contains.candidacyPeriod.type",
		    executionInterval.getName());
	}
    }

    public DegreeCandidacyForGraduatedPersonProcess getDegreeCandidacyForGraduatedPersonProcess() {
	return (DegreeCandidacyForGraduatedPersonProcess) (hasAnyCandidacyProcesses() ? getCandidacyProcesses().get(0) : null);
    }

    @Override
    public ExecutionYear getExecutionInterval() {
	return (ExecutionYear) super.getExecutionInterval();
    }
}
