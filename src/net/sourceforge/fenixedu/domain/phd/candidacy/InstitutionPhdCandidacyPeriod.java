package net.sourceforge.fenixedu.domain.phd.candidacy;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class InstitutionPhdCandidacyPeriod extends InstitutionPhdCandidacyPeriod_Base {

    protected InstitutionPhdCandidacyPeriod() {
	super();
    }

    protected InstitutionPhdCandidacyPeriod(final ExecutionYear executionYear, final DateTime start, final DateTime end,
	    final PhdCandidacyPeriodType type) {
	this();
	init(executionYear, start, end, type);
    }

    @Override
    protected void init(ExecutionYear executionYear, DateTime start, DateTime end, PhdCandidacyPeriodType type) {
	checkIfCanCreate(start, end);

	if (!PhdCandidacyPeriodType.INSTITUTION.equals(type)) {
	    throw new DomainException("error.InstitutionPhdCandidacyPeriod.type.must.be.institution");
	}

	super.init(executionYear, start, end, type);
    }

    private void checkIfCanCreate(DateTime start, DateTime end) {
	for (final CandidacyPeriod period : RootDomainObject.getInstance().getCandidacyPeriods()) {
	    if (!period.equals(this) && period.isInstitutionCandidacyPeriod() && period.intercept(start, end)) {
		throw new DomainException("error.InstitutionPhdCandidacyPeriod.already.contains.candidacyPeriod.in.given.dates");
	    }
	}
    }

    @Override
    public boolean isInstitutionCandidacyPeriod() {
	return true;
    }

    @Service
    public void addPhdProgramToPeriod(final PhdProgram phdProgram) {
	if (phdProgram == null) {
	    throw new DomainException("phd.InstitutionPhdCandidacyPeriod.phdProgram.required");
	}

	super.addPhdPrograms(phdProgram);
    }

    @Service
    public void removePhdProgramInPeriod(final PhdProgram phdProgram) {
	if (phdProgram == null) {
	    throw new DomainException("phd.InstitutionPhdCandidacyPeriod.phdProgram.required");
	}

	super.removePhdPrograms(phdProgram);
    }

    @Override
    public void addPhdPrograms(PhdProgram phdPrograms) {
	throw new DomainException("call addPhdProgramToPeriod()");
    }

    @Override
    public void removePhdPrograms(PhdProgram phdPrograms) {
	throw new DomainException("call removePhdProgramInPeriod()");
    }

    @Service
    public static InstitutionPhdCandidacyPeriod create(final PhdCandidacyPeriodBean phdCandidacyPeriodBean) {
	final ExecutionYear executionYear = phdCandidacyPeriodBean.getExecutionYear();
	final DateTime start = phdCandidacyPeriodBean.getStart();
	final DateTime end = phdCandidacyPeriodBean.getEnd();
	final PhdCandidacyPeriodType type = phdCandidacyPeriodBean.getType();

	return new InstitutionPhdCandidacyPeriod(executionYear, start, end, type);
    }

    public static InstitutionPhdCandidacyPeriod readInstitutionPhdCandidacyPeriodForDate(final DateTime date) {
	for (final CandidacyPeriod period : RootDomainObject.getInstance().getCandidacyPeriods()) {
	    if (period.isInstitutionCandidacyPeriod() && period.contains(date)) {
		return (InstitutionPhdCandidacyPeriod) period;
	    }
	}
	
	return null;
    }

    public static boolean isAnyInstitutionPhdCandidacyPeriodActive(final DateTime date) {
	return readInstitutionPhdCandidacyPeriodForDate(date) != null;
    }

    public static boolean isAnyInstitutionPhdCandidacyPeriodActive() {
	return isAnyInstitutionPhdCandidacyPeriodActive(new DateTime());
    }


    static public InstitutionPhdCandidacyPeriod getMostRecentCandidacyPeriod() {
	PhdCandidacyPeriod mostRecentCandidacyPeriod = null;

	for (CandidacyPeriod candidacyPeriod : RootDomainObject.getInstance().getCandidacyPeriods()) {
	    if (!candidacyPeriod.isInstitutionCandidacyPeriod()) {
		continue;
	    }

	    if (candidacyPeriod.getStart().isAfterNow()) {
		continue;
	    }

	    if (mostRecentCandidacyPeriod == null) {
		mostRecentCandidacyPeriod = (PhdCandidacyPeriod) candidacyPeriod;
		continue;
	    }

	    if (candidacyPeriod.getStart().isAfter(mostRecentCandidacyPeriod.getStart())) {
		mostRecentCandidacyPeriod = (PhdCandidacyPeriod) candidacyPeriod;
	    }
	}

	return (InstitutionPhdCandidacyPeriod) mostRecentCandidacyPeriod;
    }
}
