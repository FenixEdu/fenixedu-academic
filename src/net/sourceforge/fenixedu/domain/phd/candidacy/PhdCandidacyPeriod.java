package net.sourceforge.fenixedu.domain.phd.candidacy;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;

import org.joda.time.DateTime;

public class PhdCandidacyPeriod extends PhdCandidacyPeriod_Base {

    private PhdCandidacyPeriod() {
	super();
    }

    public PhdCandidacyPeriod(final ExecutionYear executionYear, final DateTime start, final DateTime end) {
	this();
	checkIfCanCreate(start, end);
	super.init(executionYear, start, end);
    }

    private void checkIfCanCreate(final DateTime start, final DateTime end) {
	for (final CandidacyPeriod period : RootDomainObject.getInstance().getCandidacyPeriods()) {
	    if (!period.equals(this) && isPhdCandidacyPeriod(period) && period.intercept(start, end)) {
		throw new DomainException("error.PhdCandidacyPeriod.already.contains.candidacyPeriod.in.given.dates");
	    }
	}
    }

    @Override
    public ExecutionYear getExecutionInterval() {
	return (ExecutionYear) super.getExecutionInterval();
    }

    static private boolean isPhdCandidacyPeriod(final CandidacyPeriod period) {
	return period.getClass().equals(PhdCandidacyPeriod.class);
    }

    static public PhdCandidacyPeriod getCandidacyPeriod(final DateTime date) {
	for (final CandidacyPeriod period : RootDomainObject.getInstance().getCandidacyPeriods()) {
	    if (isPhdCandidacyPeriod(period) && period.contains(date)) {
		return (PhdCandidacyPeriod) period;
	    }
	}
	return null;
    }
}
