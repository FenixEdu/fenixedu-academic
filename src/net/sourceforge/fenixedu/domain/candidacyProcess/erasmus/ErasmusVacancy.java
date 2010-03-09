package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

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

}
