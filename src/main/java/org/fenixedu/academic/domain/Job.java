/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.exceptions.DomainException;
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

}
