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

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.PrecedentDegreeInformation;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class Qualification extends Qualification_Base {

    static public Comparator<Qualification> COMPARATOR_BY_YEAR = new Comparator<Qualification>() {
        @Override
        public int compare(Qualification o1, Qualification o2) {

            if (o1.getDateYearMonthDay() == null && o2.getDateYearMonthDay() == null) {
                return 0;
            }

            if (o1.getDateYearMonthDay() == null) {
                return -1;
            }

            if (o2.getDateYearMonthDay() == null) {
                return 1;
            }

            int year1 = o1.getDateYearMonthDay().getYear();
            int year2 = o2.getDateYearMonthDay().getYear();

            return year1 < year2 ? -1 : (year1 == year2 ? 0 : 1);
        }
    };

    static public Comparator<Qualification> COMPARATOR_BY_MOST_RECENT_ATTENDED_END = new Comparator<Qualification>() {
        @Override
        public int compare(Qualification o1, Qualification o2) {
            if (o1.getAttendedEnd() == null) {
                return 1;
            }
            if (o2.getAttendedEnd() == null) {
                return -1;
            }
            return -1 * o1.getAttendedEnd().compareTo(o2.getAttendedEnd());
        }
    };

    public Qualification() {
        super();
        setRootDomainObject(Bennu.getInstance());
        Person personLogin = AccessControl.getPerson();
        if (personLogin != null) {
            setCreator(personLogin);
            setModifiedBy(personLogin);
        }
        setLastModificationDateDateTime(new DateTime());
        setWhenCreated(new LocalDate());
    }

    public Qualification(Person person, PrecedentDegreeInformation precedentDegreeInformation) {
        this();
        setPerson(person);
        setMark(precedentDegreeInformation.getConclusionGrade() == null ? null : precedentDegreeInformation.getConclusionGrade());
        setSchool(precedentDegreeInformation.getInstitution() == null ? null : precedentDegreeInformation.getInstitution()
                .getName());
        setDegree(precedentDegreeInformation.getDegreeDesignation() == null ? null : precedentDegreeInformation
                .getDegreeDesignation());
        setDateYearMonthDay(precedentDegreeInformation.getConclusionYear() == null ? null : new YearMonthDay(
                precedentDegreeInformation.getConclusionYear(), 1, 1));
        setCountry(precedentDegreeInformation.getCountry() == null ? null : precedentDegreeInformation.getCountry());
    }

    public Qualification(final Person person, final QualificationBean bean) {
        this();
        String[] args = {};

        if (person == null) {
            throw new DomainException("error.Qualification.invalid.person", args);
        }
        checkAttendedPartials(bean.getAttendedBegin(), bean.getAttendedEnd());

        setPerson(person);
        setType(bean.getType());
        setSchool(bean.getSchool());
        setDegree(bean.getDegree());
        setAttendedBegin(bean.getAttendedBegin());
        setAttendedEnd(bean.getAttendedEnd());
        setMark(bean.getMark());
    }

    private void checkAttendedPartials(Partial attendedBegin, Partial attendedEnd) {
        if (attendedBegin != null && attendedEnd != null && attendedBegin.isAfter(attendedEnd)) {
            throw new DomainException("error.Qualification.invalid.attended.dates");
        }
    }

    @Override
    public void setPerson(Person person) {
        /*
         * 21/04/2009 - A Qualification may be associated with a
         * IndividualCandidacy. So the person may be null
         */
        /*
         * if (person == null) { throw new
         * DomainException("The person should not be null!"); }
         */
        super.setPerson(person);
    }

    public void delete() {
        super.setPerson(null);
        super.setCreator(null);
        setModifiedBy(null);
        setCountry(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public void edit(Person person, Country country, String branch, YearMonthDay dateYearMonthDay, String degree,
            String degreeRecognition, YearMonthDay equivalenceDateYearMonthDay, String equivalenceSchool, String mark,
            String school, String specializationArea, String title, QualificationType type, String year, String designation,
            Partial attendedBegin, Partial attendedEnd) {
        Person personLogin = AccessControl.getPerson();
        if (personLogin != null) {
            setModifiedBy(personLogin);
        }
        setBranch(branch);
        setDateYearMonthDay(dateYearMonthDay);
        setDegree(degree);
        setDegreeRecognition(degreeRecognition);
        setEquivalenceDateYearMonthDay(equivalenceDateYearMonthDay);
        setEquivalenceSchool(equivalenceSchool);
        setLastModificationDateDateTime(new DateTime());
        setMark(mark);
        setSchool(school);
        setSpecializationArea(specializationArea);
        setTitle(title);
        setType(type);
        setYear(year);
        setDesignation(designation);
        setAttendedBegin(attendedBegin);
        setAttendedEnd(attendedEnd);
        setPerson(person);
        setCountry(country);

    }

    @Deprecated
    public java.util.Date getDate() {
        org.joda.time.YearMonthDay ymd = getDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setDate(java.util.Date date) {
        if (date == null) {
            setDateYearMonthDay(null);
        } else {
            setDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getEquivalenceDate() {
        org.joda.time.YearMonthDay ymd = getEquivalenceDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setEquivalenceDate(java.util.Date date) {
        if (date == null) {
            setEquivalenceDateYearMonthDay(null);
        } else {
            setEquivalenceDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
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
