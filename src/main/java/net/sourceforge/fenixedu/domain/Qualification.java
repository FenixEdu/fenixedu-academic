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

import java.util.Calendar;
import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

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

    public Qualification(Person person, Country country, InfoQualification infoQualification) {
        this();
        setPerson(person);
        if (country != null) {
            setCountry(country);
        }
        setBasicProperties(infoQualification);
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

    public void edit(InfoQualification infoQualification, Country country) {
        setBasicProperties(infoQualification);
        if (country == null) {
            setCountry(null);
        } else {
            setCountry(country);
        }
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

    private void setBasicProperties(InfoQualification infoQualification) {
        setBranch(infoQualification.getBranch());
        setDate(infoQualification.getDate());
        if (getDate() != null && !getDate().equals("")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(getDate());
            setYear(String.valueOf(calendar.get(Calendar.YEAR)));
        } else {
            setYear(null);
        }
        setDegree(infoQualification.getDegree());
        setDegreeRecognition(infoQualification.getDegreeRecognition());
        setEquivalenceDate(infoQualification.getEquivalenceDate());
        setEquivalenceSchool(infoQualification.getEquivalenceSchool());
        setMark(infoQualification.getMark());
        setSchool(infoQualification.getSchool());
        setSpecializationArea(infoQualification.getSpecializationArea());
        setTitle(infoQualification.getTitle());
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

    @Deprecated
    public boolean hasEquivalenceSchool() {
        return getEquivalenceSchool() != null;
    }

    @Deprecated
    public boolean hasBranch() {
        return getBranch() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSpecializationArea() {
        return getSpecializationArea() != null;
    }

    @Deprecated
    public boolean hasDegreeRecognition() {
        return getDegreeRecognition() != null;
    }

    @Deprecated
    public boolean hasCreator() {
        return getCreator() != null;
    }

    @Deprecated
    public boolean hasWhenCreated() {
        return getWhenCreated() != null;
    }

    @Deprecated
    public boolean hasType() {
        return getType() != null;
    }

    @Deprecated
    public boolean hasModifiedBy() {
        return getModifiedBy() != null;
    }

    @Deprecated
    public boolean hasDateYearMonthDay() {
        return getDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasCountry() {
        return getCountry() != null;
    }

    @Deprecated
    public boolean hasTitle() {
        return getTitle() != null;
    }

    @Deprecated
    public boolean hasLastModificationDateDateTime() {
        return getLastModificationDateDateTime() != null;
    }

    @Deprecated
    public boolean hasYear() {
        return getYear() != null;
    }

    @Deprecated
    public boolean hasMark() {
        return getMark() != null;
    }

    @Deprecated
    public boolean hasEquivalenceDateYearMonthDay() {
        return getEquivalenceDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

    @Deprecated
    public boolean hasAttendedEnd() {
        return getAttendedEnd() != null;
    }

    @Deprecated
    public boolean hasSchool() {
        return getSchool() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

    @Deprecated
    public boolean hasAttendedBegin() {
        return getAttendedBegin() != null;
    }

    @Deprecated
    public boolean hasDesignation() {
        return getDesignation() != null;
    }

}
