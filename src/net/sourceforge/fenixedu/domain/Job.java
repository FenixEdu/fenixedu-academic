package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

public class Job extends Job_Base {

    public Job(Person person, String employerName, String city, Country country, BusinessArea businessArea, String position,
	    LocalDate beginDate, LocalDate endDate, ContractType contractType) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());

	checkParameters(person, employerName, city, country, businessArea, position, beginDate, endDate, contractType);

	setPerson(person);
	setEmployerName(employerName);
	setCity(city);
	setCountry(country);
	setBusinessArea(businessArea);
	setPosition(position);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setContractType(contractType);
    }

    private void checkParameters(Person person, String employerName, String city, Country country, BusinessArea businessArea,
	    String position, LocalDate beginDate, LocalDate endDate, ContractType contractType) {

	if (person == null) {
	    throw new DomainException("job.creation.person.null");
	}

	if (StringUtils.isEmpty(employerName)) {
	    throw new DomainException("job.creation.employerName.null");
	}

	if (StringUtils.isEmpty(city)) {
	    throw new DomainException("job.creation.city.null");
	}

	if (country == null) {
	    throw new DomainException("job.creation.country.null");
	}

	if (businessArea == null) {
	    throw new DomainException("job.creation.businessArea.null");
	}

	if (StringUtils.isEmpty(position)) {
	    throw new DomainException("job.creation.position.null");
	}

	if (beginDate == null) {
	    throw new DomainException("job.creation.beginDate.null");
	}

	if (beginDate != null && endDate != null) {
	    if (beginDate.isAfter(endDate)) {
		throw new DomainException("job.creation.beginDate.after.endDate");
	    }
	}
    }

    public void delete() {
	removePerson();
	removeCountry();
	removeBusinessArea();
	removeRootDomainObject();
	deleteDomainObject();
    }
}
