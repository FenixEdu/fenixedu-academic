package net.sourceforge.fenixedu.domain.period;

import net.sourceforge.fenixedu.domain.AcademicPeriod;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23CandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class Over23CandidacyPeriod extends Over23CandidacyPeriod_Base {

    private Over23CandidacyPeriod() {
	super();
    }

    public Over23CandidacyPeriod(final Over23CandidacyProcess candidacyProcess, final AcademicPeriod academicPeriod,
	    final DateTime start, final DateTime end) {
	this();
	init(candidacyProcess, academicPeriod, start, end);
    }

    private void init(final Over23CandidacyProcess candidacyProcess, final AcademicPeriod academicPeriod, final DateTime start,
	    final DateTime end) {
	checkParameters(candidacyProcess);
	checkIfCanCreate(academicPeriod);
	super.init(academicPeriod, start, end);
	addCandidacyProcesses(candidacyProcess);
    }

    private void checkParameters(final Over23CandidacyProcess candidacyProcess) {
	if (candidacyProcess == null) {
	    throw new DomainException("error.Over23CandidacyPeriod.invalid.candidacy.process");
	}
    }

    private void checkIfCanCreate(final AcademicPeriod academicPeriod) {
	if (academicPeriod.getOver23CandidacyPeriod() != null) {
	    throw new DomainException("error.Over23CandidacyPeriod.academicPeriod.already.contains.candidacyPeriod.type",
		    academicPeriod.getName());
	}
    }

    public Over23CandidacyProcess getOver23CandidacyProcess() {
	return (Over23CandidacyProcess) (hasAnyCandidacyProcesses() ? getCandidacyProcesses().get(0) : null);
    }
}
