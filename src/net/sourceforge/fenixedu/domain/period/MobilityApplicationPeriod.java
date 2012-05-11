package net.sourceforge.fenixedu.domain.period;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityAgreement;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityEmailTemplate;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityEmailTemplateBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityEmailTemplateType;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityProgram;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityQuota;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class MobilityApplicationPeriod extends MobilityApplicationPeriod_Base {

    public MobilityApplicationPeriod() {
	super();
    }

    public MobilityApplicationPeriod(final MobilityApplicationProcess applicationProcess, final ExecutionYear executionInterval,
	    final DateTime start, final DateTime end) {
	this();
	init(applicationProcess, executionInterval, start, end);
    }

    private void init(final MobilityApplicationProcess applicationProcess, final ExecutionInterval executionInterval,
	    final DateTime start, final DateTime end) {
	checkParameters(applicationProcess);
	checkIfCanCreate(executionInterval, start, end);
	super.init(executionInterval, start, end);
	addCandidacyProcesses(applicationProcess);
    }

    private void checkParameters(final MobilityApplicationProcess applicationProcess) {
	if (applicationProcess == null) {
	    throw new DomainException("error.ErasmusCandidacyProcess.invalid.candidacy.process");
	}
    }

    private void checkIfCanCreate(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
	for (final MobilityApplicationPeriod mobilityApplicationPeriod : executionInterval.getMobilityApplicationPeriods()) {
	    if (mobilityApplicationPeriod.intercept(start, end)) {
		throw new DomainException("error.ErasmusCandidacyPeriod.interception", executionInterval.getName(),
			start.toString("dd/MM/yyyy HH:mm"), end.toString("dd/MM/yyyy HH:mm"));
	    }
	}
    }

    public MobilityApplicationProcess getMobilityApplicationProcess() {
	return (MobilityApplicationProcess) (hasAnyCandidacyProcesses() ? getCandidacyProcesses().get(0) : null);
    }

    @Override
    public ExecutionYear getExecutionInterval() {
	return (ExecutionYear) super.getExecutionInterval();
    }

    public String getPresentationName() {
	return getStart().toString("dd/MM/yyyy") + " - " + getEnd().toString("dd/MM/yyyy");
    }

    @Override
    public void edit(final DateTime start, final DateTime end) {
	checkDates(start, end);
	checkIfCanEdit(start, end);
	super.setStart(start);
	super.setEnd(end);
    }

    private void checkIfCanEdit(DateTime start, DateTime end) {
	for (final MobilityApplicationPeriod mobilityApplicationPeriod : getExecutionInterval().getMobilityApplicationPeriods()) {
	    if (mobilityApplicationPeriod != this && mobilityApplicationPeriod.intercept(start, end)) {
		throw new DomainException("error.ErasmusCandidacyPeriod.interception", getExecutionInterval().getName(),
			start.toString("dd/MM/yyyy HH:mm"), end.toString("dd/MM/yyyy HH:mm"));
	    }
	}
    }

    public List<Country> getAssociatedCountries() {
	Set<Country> countries = new HashSet<Country>();

	for (MobilityQuota mobilityQuota : this.getMobilityQuotas()) {
	    countries.add(mobilityQuota.getMobilityAgreement().getUniversityUnit().getCountry());
	}

	return new ArrayList<Country>(countries);
    }

    public List<MobilityQuota> getOpeningsForCountry(Country country) {
	List<MobilityQuota> openingsList = new ArrayList<MobilityQuota>();

	for (MobilityQuota quota : getMobilityQuotas()) {
	    if (quota.getMobilityAgreement().getUniversityUnit().getCountry() == country) {
		openingsList.add(quota);
	    }
	}

	return openingsList;
    }

    public List<UniversityUnit> getUniversityUnitsAssociatedToCountry(Country country) {
	Set<UniversityUnit> universityUnits = new HashSet<UniversityUnit>();

	for (MobilityQuota quota : getOpeningsForCountry(country)) {
	    universityUnits.add(quota.getMobilityAgreement().getUniversityUnit());
	}

	return new ArrayList<UniversityUnit>(universityUnits);
    }

    /**
     * @deprecated Legacy from old ErasmusVacancy entity. Use
     *             {@link #getAssociatedOpening(Degree, MobilityAgreement)}
     *             instead.
     */
    @Deprecated
    public MobilityQuota getAssociatedOpeningsToDegreeAndUniversity(Degree selectedDegree, UniversityUnit selectedUniversity) {
	if (selectedDegree == null) {
	    return null;
	}

	if (selectedUniversity == null) {
	    return null;
	}

	for (MobilityQuota quota : getOpeningsForCountry(selectedUniversity.getCountry())) {
	    if (quota.getDegree() == selectedDegree && quota.getMobilityAgreement().getUniversityUnit() == selectedUniversity) {
		return quota;
	    }
	}

	return null;
    }

    public MobilityQuota getAssociatedOpening(Degree degree, MobilityAgreement agreement) {
	if (degree == null || agreement == null) {
	    return null;
	}
	for (MobilityQuota quota : agreement.getMobilityQuotas()) {
	    if (quota.getDegree() != degree) {
		continue;
	    }
	    if (quota.getApplicationPeriod() != this) {
		continue;
	    }
	    return quota;
	}
	return null;
    }

    public List<Degree> getPossibleDegreesAssociatedToUniversity(UniversityUnit university) {
	Set<Degree> degreeSet = new HashSet<Degree>();

	for (MobilityQuota quota : getMobilityQuotas()) {
	    if (quota.getMobilityAgreement().getUniversityUnit() == university) {
		degreeSet.add(quota.getDegree());
	    }
	}

	return new ArrayList<Degree>(degreeSet);
    }

    public boolean existsFor(UniversityUnit university, Degree degree) {
	return getAssociatedOpeningsToDegreeAndUniversity(degree, university) != null;
    }

    public Set<MobilityProgram> getMobilityPrograms() {
	Set<MobilityProgram> programs = new HashSet<MobilityProgram>();

	List<MobilityQuota> mobilityQuotas = getMobilityQuotas();

	for (MobilityQuota mobilityQuota : mobilityQuotas) {
	    programs.add(mobilityQuota.getMobilityAgreement().getMobilityProgram());
	}

	return programs;
    }

    @Service
    public void editEmailTemplates(final MobilityEmailTemplateBean bean) {
	final MobilityEmailTemplateType type = bean.getType();
	final String subject = bean.getSubject();
	final String body = bean.getBody();

	for (MobilityProgram program : getMobilityPrograms()) {
	    if (!hasEmailTemplateFor(program, type)) {
		MobilityEmailTemplate.create(this, program, type, subject, body);
		return;
	    }

	    getEmailTemplateFor(program, type).update(subject, body);
	}
    }

    public List<MobilityQuota> getMobilityQuotasByProgram(final MobilityProgram program) {
	List<MobilityQuota> result = new ArrayList<MobilityQuota>();

	List<MobilityQuota> mobilityQuotas = getMobilityQuotas();

	for (MobilityQuota mobilityQuota : mobilityQuotas) {
	    if (mobilityQuota.isFor(program)) {
		result.add(mobilityQuota);
	    }
	}

	return result;
    }

    public MobilityEmailTemplate getEmailTemplateFor(final MobilityProgram program, final MobilityEmailTemplateType type) {
	for (MobilityEmailTemplate template : getEmailTemplates()) {
	    if (template.isFor(program, type)) {
		return template;
	    }
	}

	return null;
    }

    public boolean hasEmailTemplateFor(final MobilityProgram program, final MobilityEmailTemplateType type) {
	return getEmailTemplateFor(program, type) != null;
    }

    public MobilityEmailTemplate getEmailTemplateFor(final MobilityEmailTemplateType type) {
	MobilityProgram mobilityProgram = getMobilityPrograms().iterator().next();

	return getEmailTemplateFor(mobilityProgram, type);
    }

    public boolean hasEmailTemplateFor(final MobilityEmailTemplateType type) {
	MobilityProgram mobilityProgram = getMobilityPrograms().iterator().next();
	return hasEmailTemplateFor(mobilityProgram, type);
    }

}
