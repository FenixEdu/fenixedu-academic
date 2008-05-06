package net.sourceforge.fenixedu.domain.period;

import net.sourceforge.fenixedu.domain.AcademicPeriod;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class SecondCycleCandidacyPeriod extends SecondCycleCandidacyPeriod_Base {

    private SecondCycleCandidacyPeriod() {
	super();
    }

    public SecondCycleCandidacyPeriod(final SecondCycleCandidacyProcess candidacyProcess, final AcademicPeriod academicPeriod,
	    final DateTime start, final DateTime end) {
	this();
	init(candidacyProcess, academicPeriod, start, end);
    }

    private void init(final SecondCycleCandidacyProcess candidacyProcess, final AcademicPeriod academicPeriod, final DateTime start,
	    final DateTime end) {
	checkParameters(candidacyProcess);
	checkIfCanCreate(academicPeriod);
	super.init(academicPeriod, start, end);
	addCandidacyProcesses(candidacyProcess);
    }

    private void checkParameters(final SecondCycleCandidacyProcess candidacyProcess) {
	if (candidacyProcess == null) {
	    throw new DomainException("error.SecondCycleCandidacyPeriod.invalid.candidacy.process");
	}
    }

    private void checkIfCanCreate(final AcademicPeriod academicPeriod) {
	if (academicPeriod.getSecondCycleCandidacyPeriod() != null) {
	    throw new DomainException("error.SecondCycleCandidacyPeriod.academicPeriod.already.contains.candidacyPeriod.type",
		    academicPeriod.getName());
	}
    }

    public SecondCycleCandidacyProcess getSecondCycleCandidacyProcess() {
	return (SecondCycleCandidacyProcess) (hasAnyCandidacyProcesses() ? getCandidacyProcesses().get(0) : null);
    }
}
