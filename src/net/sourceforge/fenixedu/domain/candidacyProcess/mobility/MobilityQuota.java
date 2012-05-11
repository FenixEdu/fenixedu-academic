package net.sourceforge.fenixedu.domain.candidacyProcess.mobility;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.period.MobilityApplicationPeriod;
import pt.ist.fenixWebFramework.services.Service;

public class MobilityQuota extends MobilityQuota_Base {

    public MobilityQuota() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public MobilityQuota(MobilityApplicationPeriod period, Degree degree, MobilityAgreement mobilityAgreement,
	    Integer numberOfOpenings) {
	this();
	setApplicationPeriod(period);
	setDegree(degree);
	setMobilityAgreement(mobilityAgreement);
	setNumberOfOpenings(numberOfOpenings);
	check();
    }

    public MobilityQuota(final MobilityApplicationPeriod period, Degree degree, MobilityProgram mobilityProgram,
	    UniversityUnit unit, Integer numberOfOpenings) {
	this();

	setApplicationPeriod(period);
	setDegree(degree);

	MobilityAgreement agreement = MobilityAgreement.getOrCreateAgreement(mobilityProgram, unit);

	setMobilityAgreement(agreement);

	setNumberOfOpenings(numberOfOpenings);

	check();
    }

    private void check() {
	if (getApplicationPeriod() == null) {
	    throw new DomainException("error.erasmus.vacancy.candidacy.period.must.not.be.null");
	}

	if (getDegree() == null) {
	    throw new DomainException("error.erasmus.vacancy.degree.must.not.be.null");
	}

	if (getMobilityAgreement().getUniversityUnit() == null) {
	    throw new DomainException("error.erasmus.vacancy.university.unit.must.not.be.null");
	}

	if (getNumberOfOpenings() == null) {
	    throw new DomainException("error.erasmus.vacancy.number.of.vacancies.must.not.be.null");
	}
    }

    @Service
    public static MobilityQuota createVacancy(final MobilityApplicationPeriod period, final Degree degree,
	    final MobilityProgram mobilityProgram, final UniversityUnit unit, final Integer numberOfOpenings) {
	return new MobilityQuota(period, degree, mobilityProgram, unit, numberOfOpenings);
    }

    public List<MobilityIndividualApplicationProcess> getStudentApplicationProcesses() {
	List<MobilityIndividualApplicationProcess> processList = new ArrayList<MobilityIndividualApplicationProcess>();

	for (MobilityStudentData data : getApplications()) {
	    processList.add(data.getMobilityIndividualApplication().getCandidacyProcess());
	}

	return processList;
    }

    public boolean isQuotaAssociatedWithAnyApplication() {
	return hasAnyApplications();
    }

    public void delete() {
	if (isQuotaAssociatedWithAnyApplication()) {
	    throw new DomainException("error.mobility.quota.is.associated.with.applications");
	}

	removeMobilityAgreement();
	removeDegree();
	removeApplicationPeriod();
	removeRootDomainObject();

	deleteDomainObject();
    }

    public boolean isFor(final MobilityProgram mobilityProgram) {
	return getMobilityAgreement().getMobilityProgram() == mobilityProgram;
    }

    public boolean isAssociatedToApplications() {
	return !getStudentApplicationProcesses().isEmpty();
    }

}
