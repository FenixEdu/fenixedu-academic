/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniJobBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
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
        setRootDomainObject(Bennu.getInstance());
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
        String[] args = {};
        if (person == null) {
            throw new DomainException("job.creation.person.null", args);
        }
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

        String[] args = {};
        if (person == null) {
            throw new DomainException("job.creation.person.null", args);
        }
        String[] args1 = {};
        if (country == null) {
            throw new DomainException("job.creation.country.null", args1);
        }
        String[] args2 = {};
        if (businessArea == null) {
            throw new DomainException("job.creation.businessArea.null", args2);
        }
        String[] args3 = {};

        if (employerName == null || employerName.isEmpty()) {
            throw new DomainException("job.creation.employerName.null", args3);
        }
        String[] args4 = {};
        if (city == null || city.isEmpty()) {
            throw new DomainException("job.creation.city.null", args4);
        }
        String[] args5 = {};
        if (position == null || position.isEmpty()) {
            throw new DomainException("job.creation.position.null", args5);
        }
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
        setPerson(null);
        setCreator(null);
        setCountry(null);
        setBusinessArea(null);
        setParentBusinessArea(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasBeginDate() {
        return getBeginDate() != null;
    }

    @Deprecated
    public boolean hasContractType() {
        return getContractType() != null;
    }

    @Deprecated
    public boolean hasCreator() {
        return getCreator() != null;
    }

    @Deprecated
    public boolean hasSalaryType() {
        return getSalaryType() != null;
    }

    @Deprecated
    public boolean hasParentBusinessArea() {
        return getParentBusinessArea() != null;
    }

    @Deprecated
    public boolean hasLastModifiedDate() {
        return getLastModifiedDate() != null;
    }

    @Deprecated
    public boolean hasCountry() {
        return getCountry() != null;
    }

    @Deprecated
    public boolean hasCity() {
        return getCity() != null;
    }

    @Deprecated
    public boolean hasJobApplicationType() {
        return getJobApplicationType() != null;
    }

    @Deprecated
    public boolean hasEmployerName() {
        return getEmployerName() != null;
    }

    @Deprecated
    public boolean hasEndDate() {
        return getEndDate() != null;
    }

    @Deprecated
    public boolean hasBusinessArea() {
        return getBusinessArea() != null;
    }

    @Deprecated
    public boolean hasPosition() {
        return getPosition() != null;
    }

    @Deprecated
    public boolean hasSalary() {
        return getSalary() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
