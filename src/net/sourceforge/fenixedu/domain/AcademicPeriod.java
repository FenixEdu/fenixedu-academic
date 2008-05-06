package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.period.Over23CandidacyPeriod;
import net.sourceforge.fenixedu.domain.period.SecondCycleCandidacyPeriod;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.util.PeriodState;

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

    public List<? extends CandidacyPeriod> getCandidacyPeriod(final Class<? extends CandidacyPeriod> clazz) {
	final List<CandidacyPeriod> result = new ArrayList<CandidacyPeriod>();
	for (final CandidacyPeriod candidacyPeriod : getCandidacyPeriods()) {
	    if (candidacyPeriod.getClass().equals(clazz)) {
		result.add(candidacyPeriod);
	    }
	}
	return result;
    }

    public Over23CandidacyPeriod getOver23CandidacyPeriod() {
	final List<Over23CandidacyPeriod> candidacyPeriods = (List<Over23CandidacyPeriod>) getCandidacyPeriod(Over23CandidacyPeriod.class);
	return candidacyPeriods.isEmpty() ? null : candidacyPeriods.get(0);
    }

    public SecondCycleCandidacyPeriod getSecondCycleCandidacyPeriod() {
	final List<SecondCycleCandidacyPeriod> candidacyPeriods = (List<SecondCycleCandidacyPeriod>) getCandidacyPeriod(SecondCycleCandidacyPeriod.class);
	return candidacyPeriods.isEmpty() ? null : candidacyPeriods.get(0);
    }
}
