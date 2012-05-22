package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

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

    public static List<PhdCandidacyPeriod> readOrderedPhdCandidacyPeriods() {
	List<PhdCandidacyPeriod> phdCandidacyPeriods = readPhdCandidacyPeriods();

	Collections.sort(phdCandidacyPeriods, Collections.reverseOrder(CandidacyPeriod.LAST_CANDIDACY_PERIOD));

	return phdCandidacyPeriods;
    }

    protected void checkOverlapingDates(DateTime start, DateTime end, PhdCandidacyPeriodType type) {
	for (final CandidacyPeriod period : RootDomainObject.getInstance().getCandidacyPeriods()) {

	    if (!period.isPhdCandidacyPeriod()) {
		continue;
	    }

	    PhdCandidacyPeriod phdCandidacyPeriod = (PhdCandidacyPeriod) period;

	    if (!period.equals(this) && type.equals(phdCandidacyPeriod.getType()) && period.intercept(start, end)) {
		throw new DomainException("error.InstitutionPhdCandidacyPeriod.already.contains.candidacyPeriod.in.given.dates");
	    }
	}
    }

    @Service
    @Override
    public void edit(final DateTime start, final DateTime end) {
	checkOverlapingDates(start, end, getType());
	super.edit(start, end);
    }

    public abstract String getEmailMessageBodyForRefereeForm(final PhdCandidacyReferee referee);

    public abstract MultiLanguageString getEmailMessageSubjectForMissingCandidacyValidation(
	    final PhdIndividualProgramProcess process);

    public abstract MultiLanguageString getEmailMessageBodyForMissingCandidacyValidation(final PhdIndividualProgramProcess process);

    @Override
    public String getPresentationName() {
	return getType().getLocalizedName() + " - " + getExecutionInterval().getName() + " - " + super.getPresentationName();
    }

    public String getStartDateDescription() {
	return getStart().toString("dd/MM/yyyy");
    }

    public String getEndDateDescription() {

	if (getEnd() == null) {
	    return "";
	}

	return getEnd().toString("dd/MM/yyyy");
    }
}
