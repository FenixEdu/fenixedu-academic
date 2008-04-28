package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.util.PeriodState;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class AcademicPeriod extends AcademicPeriod_Base {

    public AcademicPeriod() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setState(PeriodState.NOT_OPEN);
    }

    @Override
    public void setExecutionInterval(AcademicInterval executionInterval) {
	if (executionInterval == null) {
	    throw new DomainException("error.AcademicPeriod.empty.executionInterval");
	}
	super.setExecutionInterval(executionInterval);
    }

    @Override
    public void setState(PeriodState state) {
	if (state == null) {
	    throw new DomainException("error.AcademicPeriod.empty.state");
	}
	super.setState(state);
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateInterval() {
	final YearMonthDay start = getBeginDateYearMonthDay();
	final YearMonthDay end = getEndDateYearMonthDay();
	return start != null && end != null && start.isBefore(end);
    }

    public List<CandidacyProcess> getCandidacyProcesses(final Class<? extends CandidacyProcess> clazz, final DateTime when) {
	final List<CandidacyProcess> result = new ArrayList<CandidacyProcess>();
	for (final CandidacyPeriod candidacyPeriod : getCandidacyPeriods()) {
	    if (candidacyPeriod.isOpen(when)) {
		result.addAll(candidacyPeriod.getCandidacyProcesses(clazz));
	    }
	}
	return result;
    }
}
