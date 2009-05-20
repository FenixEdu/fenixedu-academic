package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

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

	checkParameters(person, employerName, city, country, businessArea, position);
	checkDates(beginDate, endDate);

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
	    String position) {

	check(person, "job.creation.person.null");
	check(country, "job.creation.country.null");
	check(businessArea, "job.creation.businessArea.null");

	check(employerName, "job.creation.employerName.null");
	check(city, "job.creation.city.null");
	check(position, "job.creation.position.null");
    }

    private void checkDates(LocalDate beginDate, LocalDate endDate) {
	if (beginDate == null) {
	    throw new DomainException("job.creation.beginDate.null");
	}

	if (beginDate != null && endDate != null) {
	    if (beginDate.isAfter(endDate)) {
		throw new DomainException("job.creation.beginDate.after.endDate");
	    }
	}
    }

    public Job(final Person person, final JobBean bean) {
	this();

	checkParameters(person, bean.getEmployerName(), bean.getCity(), bean.getCountry(), bean.getChildBusinessArea(), bean
		.getPosition());
	checkDates(bean.getBeginDate(), bean.getEndDate());

	setPerson(person);
	setBusinessArea(bean.getChildBusinessArea());
	setEmployerName(bean.getEmployerName());
	setCity(bean.getCity());
	setPosition(bean.getPosition());
	setBeginDate(bean.getBeginDate());
	setEndDate(bean.getEndDate());
	setCountry(bean.getCountry());
    }

    public void delete() {
	removePerson();
	removeCountry();
	removeBusinessArea();
	removeRootDomainObject();
	deleteDomainObject();
    }
}
