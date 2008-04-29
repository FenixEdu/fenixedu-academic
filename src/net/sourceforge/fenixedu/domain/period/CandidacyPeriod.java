package net.sourceforge.fenixedu.domain.period;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.AcademicPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

abstract public class CandidacyPeriod extends CandidacyPeriod_Base {

    protected CandidacyPeriod() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    protected void init(final AcademicPeriod academicPeriod, final DateTime start, final DateTime end) {
	checkParameters(academicPeriod, start, end);
	setAcademicPeriod(academicPeriod);
	setStart(start);
	setEnd(end);
    }

    protected void checkParameters(final AcademicPeriod academicPeriod, final DateTime start, final DateTime end) {
	if (academicPeriod == null) {
	    throw new DomainException("error.CandidacyPeriod.invalid.academic.period");
	}
	checkDates(start, end);
    }

    private void checkDates(final DateTime start, final DateTime end) {
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

    public boolean hasCandidacyProcesses(final Class<? extends CandidacyProcess> clazz, final AcademicPeriod academicPeriod) {
	return hasAcademicPeriod(academicPeriod) && containsCandidacyProcess(clazz);
    }

    public boolean hasAcademicPeriod(final AcademicPeriod academicPeriod) {
	return getAcademicPeriod() == academicPeriod;
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
