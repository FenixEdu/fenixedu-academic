package net.sourceforge.fenixedu.domain;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.commons.lang.StringUtils;

public class Formation extends Formation_Base {

    public Formation() {
	super();
    }

    public Formation(Person person, FormationType formationType, QualificationType degree, EducationArea educationArea,
	    String beginYear, String endYear, BigDecimal ectsCredits, Integer formationHours, AcademicalInstitutionUnit institution) {
	this();

	checkParameters(person, formationType, degree, educationArea, beginYear, endYear, ectsCredits, formationHours,
		institution);

	setPerson(person);
	setFormationType(formationType);
	setDegree(degree.getName());
	setType(degree);
	setEducationArea(educationArea);
	setBeginYear(beginYear);
	setYear(endYear);
	setInstitution(institution);
	setEctsCredits(ectsCredits);
	setFormationHours(formationHours);
    }

    private void checkParameters(Person person, FormationType formationType, QualificationType degree,
	    EducationArea educationArea, String beginYear, String endYear, BigDecimal ectsCredits, Integer formationHours,
	    AcademicalInstitutionUnit institution) {

	if (person == null) {
	    throw new DomainException("formation.creation.person.null");
	}

	if (formationType == null) {
	    throw new DomainException("formation.creation.formationType.null");
	}

	if (degree == null) {
	    throw new DomainException("formation.creation.degree.null");
	}

	if (educationArea == null) {
	    throw new DomainException("formation.creation.educationArea.null");
	}

	if (institution == null) {
	    throw new DomainException("formation.creation.institution.null");
	}

	if (StringUtils.isEmpty(beginYear)) {
	    throw new DomainException("formation.creation.beginYear.null");
	}

	if (!StringUtils.isEmpty(beginYear) && !StringUtils.isEmpty(endYear)) {
	    if (Integer.parseInt(beginYear) > Integer.parseInt(endYear)) {
		throw new DomainException("formation.creation.beginDate.after.endDate");
	    }
	}
    }

    public void delete() {
	removePerson();
	removeCountry();
	removeEducationArea();
	removeInstitution();
	removeRootDomainObject();
	deleteDomainObject();
    }

}
