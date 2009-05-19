package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

public class Job extends Job_Base {

    static final public Comparator<Job> REVERSE_COMPARATOR_BY_BEGIN_DATE = new Comparator<Job>() {
	public int compare(final Job o1, final Job o2) {
	    return o2.getBeginDate().compareTo(o1.getBeginDate());
	}
    };

    private Job() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Job(Person person, String employerName, String city, Country country, BusinessArea businessArea, String position,
	    LocalDate beginDate, LocalDate endDate, ContractType contractType) {
	this(person, employerName, city, country, businessArea, position, beginDate, endDate, null, contractType, null);
    }

    public Job(Person person, String employerName, String city, Country country, BusinessArea businessArea, String position,
	    LocalDate beginDate, LocalDate endDate, JobApplicationType applicationType, ContractType contractType,
	    SalaryType salaryType) {
	
	this();

	checkParameters(person, employerName, city, country, businessArea, position, beginDate, endDate);

	setPerson(person);
	setEmployerName(employerName);
	setCity(city);
	setCountry(country);
	setBusinessArea(businessArea);
	setPosition(position);
	setBeginDate(beginDate);
	setEndDate(endDate);
	setJobApplicationType(applicationType);
	setContractType(contractType);
	setSalaryType(salaryType);
    }

    private void checkParameters(Person person, String employerName, String city, Country country, BusinessArea businessArea,
	    String position, LocalDate beginDate, LocalDate endDate) {

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
