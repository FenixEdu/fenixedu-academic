package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public abstract class PhdCandidacyPeriod extends PhdCandidacyPeriod_Base {

    protected PhdCandidacyPeriod() {
	super();
    }

    @Override
    protected void init(ExecutionInterval executionInterval, DateTime start, DateTime end) {
	throw new DomainException("call init(ExecutionYear, DateTime, DateTime)");
    }

    protected void init(final ExecutionYear executionInterval, final DateTime start, final DateTime end,
	    PhdCandidacyPeriodType type) {
	if (type == null) {
	    throw new DomainException("error.PhdCandidacyPeriod.type.is.required");
	}

	setType(type);

	super.init(executionInterval, start, end);
    }

    @Override
    public ExecutionYear getExecutionInterval() {
	return (ExecutionYear) super.getExecutionInterval();
    }

    @Override
    public boolean isPhdCandidacyPeriod() {
	return true;
    }

    public static List<PhdCandidacyPeriod> readPhdCandidacyPeriods() {
	List<PhdCandidacyPeriod> phdCandidacyPeriods = new ArrayList<PhdCandidacyPeriod>();

	for (CandidacyPeriod candidacyPeriod : RootDomainObject.getInstance().getCandidacyPeriods()) {
	    if (candidacyPeriod.isPhdCandidacyPeriod()) {
		phdCandidacyPeriods.add((PhdCandidacyPeriod) candidacyPeriod);
	    }
	}

	return phdCandidacyPeriods;
    }

    @Service
    @Override
    public void edit(final DateTime start, final DateTime end) {
	super.edit(start, end);
    }

    public abstract String getEmailMessageBodyForRefereeForm(final PhdCandidacyReferee referee);

    @Override
    public String getPresentationName() {
	return getType().getLocalizedName() + " - " + getExecutionInterval().getName() + " - " + super.getPresentationName();
    }
}
