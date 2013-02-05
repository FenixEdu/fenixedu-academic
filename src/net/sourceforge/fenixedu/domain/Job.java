package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniJobBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class Job extends Job_Base {

    static final public Comparator<Job> REVERSE_COMPARATOR_BY_BEGIN_DATE = new Comparator<Job>() {
        @Override
        public int compare(final Job o1, final Job o2) {
            if (o2.getBeginDate() != null && o1.getBeginDate() != null) {
                return o2.getBeginDate().compareTo(o1.getBeginDate());
            } else {
                return o2.getBeginDate() != null ? 1 : -1;
            }
        }
    };

    private Job() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setLastModifiedDate(new DateTime());
    }

    public Job(Person person, String employerName, String city, Country country, BusinessArea businessArea,
            BusinessArea parentBusinessArea, String position, LocalDate beginDate, LocalDate endDate,
            JobApplicationType applicationType, ContractType contractType, Double salary) {

        this();

        checkParameters(person, employerName, city, country, businessArea, parentBusinessArea, position, beginDate, endDate,
                applicationType, contractType, salary);
        checkValidDates(beginDate, endDate);

        setPerson(person);
        setEmployerName(employerName);
        setCity(city);
        setCountry(country);
        setBusinessArea(businessArea);
        setParentBusinessArea(parentBusinessArea);
        setPosition(position);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setJobApplicationType(applicationType);
        setContractType(contractType);
        //TODO remove setSalaryType(salaryType);
        setSalary(salary);
    }

    public void edit(final AlumniJobBean jobBean) {
        checkParameters(jobBean.getEmployerName(), jobBean.getCity(), jobBean.getCountry(), jobBean.getChildBusinessArea(),
                jobBean.getParentBusinessArea(), jobBean.getPosition(), jobBean.getBeginDateAsLocalDate(),
                jobBean.getEndDateAsLocalDate(), jobBean.getApplicationType(), jobBean.getContractType(), jobBean.getSalary());
        setEmployerName(jobBean.getEmployerName());
        setCity(jobBean.getCity());
        setCountry(jobBean.getCountry());
        setBusinessArea(jobBean.getChildBusinessArea());
        setParentBusinessArea(jobBean.getParentBusinessArea());
        setPosition(jobBean.getPosition());
        setBeginDate(jobBean.getBeginDateAsLocalDate());
        setEndDate(jobBean.getEndDateAsLocalDate());
        setJobApplicationType(jobBean.getApplicationType());
        setContractType(jobBean.getContractType());
        //TODO remove setSalaryType(jobBean.getSalaryType());
        setSalary(jobBean.getSalary());
        setLastModifiedDate(new DateTime());
    }

    private void checkParameters(Person person, String employerName, String city, Country country, BusinessArea businessArea,
            BusinessArea parentBusinessArea, String position, LocalDate beginDate, LocalDate endDate,
            JobApplicationType applicationType, ContractType contractType, Double salary) {
        check(person, "job.creation.person.null");
        checkParameters(employerName, city, country, businessArea, parentBusinessArea, position, beginDate, endDate,
                applicationType, contractType, salary);
    }

    private void checkParameters(String employerName, String city, Country country, BusinessArea businessArea,
            BusinessArea parentBusinessArea, String position, LocalDate beginDate, LocalDate endDate,
            JobApplicationType applicationType, ContractType contractType, Double salary) {
        if (StringUtils.isEmpty(employerName) && StringUtils.isEmpty(city) && country == null && businessArea == null
                && parentBusinessArea == null && StringUtils.isEmpty(position) && beginDate == null && endDate == null
                && applicationType == null && contractType == null && salary == null) {
            throw new DomainException("job.creation.allFields.null");
        }
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

        checkValidDates(beginDate, endDate);
    }

    private void checkValidDates(LocalDate beginDate, LocalDate endDate) {
        if (beginDate != null && endDate != null) {
            if (beginDate.isAfter(endDate)) {
                throw new DomainException("job.creation.beginDate.after.endDate");
            }
        }
    }

    public Job(final Person person, final JobBean bean) {
        this();

        checkParameters(person, bean.getEmployerName(), bean.getCity(), bean.getCountry(), bean.getChildBusinessArea(),
                bean.getPosition());
        checkDates(bean.getBeginDate(), bean.getEndDate());

        setPerson(person);
        setBusinessArea(bean.getChildBusinessArea());
        setParentBusinessArea(bean.getParentBusinessArea());
        setEmployerName(bean.getEmployerName());
        setCity(bean.getCity());
        setPosition(bean.getPosition());
        setBeginDate(bean.getBeginDate());
        setEndDate(bean.getEndDate());
        setCountry(bean.getCountry());
    }

    public void delete() {
        removePerson();
        removeCreator();
        removeCountry();
        removeBusinessArea();
        removeParentBusinessArea();
        removeRootDomainObject();
        deleteDomainObject();
    }
}
