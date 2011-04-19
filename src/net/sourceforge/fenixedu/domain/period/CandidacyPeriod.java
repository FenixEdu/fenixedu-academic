package net.sourceforge.fenixedu.domain.period;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

abstract public class CandidacyPeriod extends CandidacyPeriod_Base {

    static final public Comparator<CandidacyPeriod> LAST_CANDIDACY_PERIOD = new Comparator<CandidacyPeriod>() {
	@Override
	public int compare(CandidacyPeriod o1, CandidacyPeriod o2) {
	    int result = o1.getStart().compareTo(o2.getStart());
	    return -1 * ((result == 0) ? o1.getEnd().compareTo(o2.getEnd()) : result);
	}
    };

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
	check(executionInterval, "error.CandidacyPeriod.invalid.academic.period");
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

    public boolean intercept(final DateTime start, final DateTime end) {
	checkDates(start, end);
	return contains(start) || contains(end);
    }

    public boolean isPhdCandidacyPeriod() {
	return false;
    }

    public boolean isEpflCandidacyPeriod() {
	return false;
    }

    public boolean isInstitutionCandidacyPeriod() {
	return false;
    }

    public String getPresentationName() {
	return getStart().toString("dd/MM/yyyy") + " - " + getEnd().toString("dd/MM/yyyy");
    }
}
