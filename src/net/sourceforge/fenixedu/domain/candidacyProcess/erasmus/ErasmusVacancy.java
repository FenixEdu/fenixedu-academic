package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.period.ErasmusCandidacyPeriod;
import pt.ist.fenixWebFramework.services.Service;

public class ErasmusVacancy extends ErasmusVacancy_Base {
    
    private ErasmusVacancy() {
        super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public ErasmusVacancy(final ErasmusCandidacyPeriod period, Degree degree, UniversityUnit unit, Integer numberOfVacancies) {
	this();

	setCandidacyPeriod(period);
	setDegree(degree);
	setUniversityUnit(unit);

	setNumberOfVacancies(numberOfVacancies);

	check();
    }

    private void check() {
	if (getCandidacyPeriod() == null) {
	    throw new DomainException("error.erasmus.vacancy.candidacy.period.must.not.be.null");
	}

	if (getDegree() == null) {
	    throw new DomainException("error.erasmus.vacancy.degree.must.not.be.null");
	}

	if (getUniversityUnit() == null) {
	    throw new DomainException("error.erasmus.vacancy.university.unit.must.not.be.null");
	}

	if (getNumberOfVacancies() == null) {
	    throw new DomainException("error.erasmus.vacancy.number.of.vacancies.must.not.be.null");
	}
    }

    @Service
    public static ErasmusVacancy createVacancy(final ErasmusCandidacyPeriod period, Degree degree, UniversityUnit unit,
	    Integer numberOfVacancies) {
	return new ErasmusVacancy(period, degree, unit, numberOfVacancies);
    }

    public List<ErasmusIndividualCandidacyProcess> getStudentApplicationProcesses() {
	List<ErasmusIndividualCandidacyProcess> processList = new ArrayList<ErasmusIndividualCandidacyProcess>();

	for (ErasmusStudentData data : getCandidacies()) {
	    processList.add(data.getErasmusIndividualCandidacy().getCandidacyProcess());
	}

	return processList;
    }

    public boolean isVacancyAssociatedToAnyCandidacy() {
	return hasAnyCandidacies();
    }

    public void delete() {
	if (isVacancyAssociatedToAnyCandidacy()) {
	    throw new DomainException("error.erasmus.vacancy.is.associated.to.candidacies");
	}

	removeUniversityUnit();
	removeDegree();
	removeCandidacyPeriod();
	removeRootDomainObject();

	deleteDomainObject();
    }

}
