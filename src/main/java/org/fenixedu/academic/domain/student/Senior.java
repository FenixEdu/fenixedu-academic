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
/*
 * Created on Dec 10, 2004
 *
 */
package org.fenixedu.academic.domain.student;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

/**
 * @author Luis Egidio, luis.egidio@ist.utl.pt
 * 
 */
public class Senior extends Senior_Base {

    public Senior(final Registration registration) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setRegistration(registration);
    }

    public void delete() {
        if (isEmpty()) {
            super.setStudent(null);
            setRootDomainObject(null);
            deleteDomainObject();
        } else {
            throw new DomainException("error.Senior.not.empty");
        }
    }

    public boolean isEmpty() {
        return getExpectedDegreeAverageGrade() == null && getExpectedDegreeTerminationDateTime() == null
                && (getExtracurricularActivities() == null || StringUtils.isEmpty(getExtracurricularActivities().trim()))
                && (getInformaticsSkills() == null || StringUtils.isEmpty(getInformaticsSkills().trim()))
                && (getLanguageSkills() == null || StringUtils.isEmpty(getLanguageSkills().trim()))
                && (getProfessionalExperience() == null || StringUtils.isEmpty(getProfessionalExperience().trim()))
                && (getProfessionalInterests() == null || StringUtils.isEmpty(getProfessionalInterests().trim()))
                && (getSpecialtyField() == null || StringUtils.isEmpty(getSpecialtyField().trim()));
    }

    public void setExpectedDegreeTerminationYearMonthDay(YearMonthDay date) {
        setExpectedDegreeTerminationDateTime(date != null ? date.toDateTimeAtMidnight() : null);
    }

    public YearMonthDay getExpectedDegreeTerminationYearMonthDay() {
        return getExpectedDegreeTerminationDateTime() != null ? getExpectedDegreeTerminationDateTime().toYearMonthDay() : null;
    }

    @Override
    @Deprecated
    public void setStudent(final Registration registration) {
        this.setRegistration(registration);
    }

    public void setRegistration(final Registration registration) {
        if (registration == null) {
            throw new DomainException("error.senior.empty.senior");
        }

        super.setStudent(registration);
    }

    @Override
    @Deprecated
    public Registration getStudent() {
        return this.getRegistration();
    }

    public Registration getRegistration() {
        return super.getStudent();
    }

    public Person getPerson() {
        return getRegistration().getPerson();
    }

    @Override
    public void setExpectedDegreeAverageGrade(Integer expectedDegreeAverageGrade) {
        setLastModificationDateDateTime(new DateTime());
        super.setExpectedDegreeAverageGrade(expectedDegreeAverageGrade);
    }

    @Override
    public void setExpectedDegreeTerminationDateTime(DateTime expectedDegreeTerminationDateTime) {
        setLastModificationDateDateTime(new DateTime());
        super.setExpectedDegreeTerminationDateTime(expectedDegreeTerminationDateTime);
    }

    @Override
    public void setExtracurricularActivities(String extracurricularActivities) {
        setLastModificationDateDateTime(new DateTime());
        super.setExtracurricularActivities(extracurricularActivities);
    }

    @Override
    public void setInformaticsSkills(String informaticsSkills) {
        setLastModificationDateDateTime(new DateTime());
        super.setInformaticsSkills(informaticsSkills);
    }

    @Override
    public void setLanguageSkills(String languageSkills) {
        setLastModificationDateDateTime(new DateTime());
        super.setLanguageSkills(languageSkills);
    }

    @Override
    public void setProfessionalExperience(String professionalExperience) {
        setLastModificationDateDateTime(new DateTime());
        super.setProfessionalExperience(professionalExperience);
    }

    @Override
    public void setProfessionalInterests(String professionalInterests) {
        setLastModificationDateDateTime(new DateTime());
        super.setProfessionalInterests(professionalInterests);
    }

    @Override
    public void setSpecialtyField(String specialtyField) {
        setLastModificationDateDateTime(new DateTime());
        super.setSpecialtyField(specialtyField);
    }

    public boolean isSenior(ExecutionYear executionYear) {
        return getExpectedDegreeTerminationYearMonthDay().isAfter(executionYear.getBeginDateYearMonthDay());
    }

    @Deprecated
    public java.util.Date getExpectedDegreeTermination() {
        org.joda.time.DateTime dt = getExpectedDegreeTerminationDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setExpectedDegreeTermination(java.util.Date date) {
        if (date == null) {
            setExpectedDegreeTerminationDateTime(null);
        } else {
            setExpectedDegreeTerminationDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public java.util.Date getLastModificationDate() {
        org.joda.time.DateTime dt = getLastModificationDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setLastModificationDate(java.util.Date date) {
        if (date == null) {
            setLastModificationDateDateTime(null);
        } else {
            setLastModificationDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

}
