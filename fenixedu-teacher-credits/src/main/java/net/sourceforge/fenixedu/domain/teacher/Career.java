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
/*
 * Created on 13/Nov/2003
 *  
 */
package org.fenixedu.academic.domain.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.CareerType;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.Interval;
import org.joda.time.LocalDateTime;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.PossiblyNullEndedInterval;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
//
public abstract class Career extends Career_Base {

    public static Comparator<Career> CAREER_DATE_COMPARATOR = new Comparator<Career>() {
        @Override
        public int compare(Career o1, Career o2) {
            if (!o1.getBeginYear().equals(o2.getBeginYear())) {
                return -(o1.getBeginYear().compareTo(o2.getBeginYear()));
            } else {
                return o1.getExternalId().compareTo(o2.getExternalId());
            }
        }
    };

    public Career() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    @Atomic
    public void delete() {
        setPerson(null);
        setTeacher(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public PossiblyNullEndedInterval getInterval() {
        if (getEndYear() != null) {
            return new PossiblyNullEndedInterval(new LocalDateTime(getBeginYear(), 1, 1, 0, 0, 0).toDateTime().getMillis(),
                    new LocalDateTime(getEndYear(), 1, 1, 0, 0, 0).toDateTime().getMillis());
        } else {
            return new PossiblyNullEndedInterval(new LocalDateTime(getBeginYear(), 1, 1, 0, 0, 0).toDateTime().getMillis());
        }
    }

    @Override
    public void setBeginYear(Integer beginYear) {
        if (getEndYear() != null && beginYear != null && beginYear > getEndYear()) {
            throw new DomainException("error.professionalcareer.endYearBeforeStart");
        }
        super.setBeginYear(beginYear);
    }

    @Override
    public void setEndYear(Integer endYear) {
        if (getBeginYear() != null && endYear != null && getBeginYear() > endYear) {
            throw new DomainException("error.professionalcareer.endYearBeforeStart");
        }
        super.setEndYear(endYear);
    }

    public static Collection<Career> readAllByTeacherIdAndCareerType(Person person, CareerType careerType) {
        if (careerType == null) {
            return person.getAssociatedCareersSet();
        }
        List<Career> allTeacherCareers = new ArrayList<Career>();

        if (careerType.equals(CareerType.PROFESSIONAL)) {
            readCareersByClass(person, allTeacherCareers, ProfessionalCareer.class.getName());
        } else if (careerType.equals(CareerType.TEACHING)) {
            readCareersByClass(person, allTeacherCareers, TeachingCareer.class.getName());
        }
        return allTeacherCareers;
    }

    private static void readCareersByClass(Person person, List<Career> allTeacherCareers, String className) {
        for (Career career : person.getAssociatedCareersSet()) {
            if (career.getClass().getName().equals(className)) {
                allTeacherCareers.add(career);
            }
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

    public static Set<Career> getCareersByType(Person person, CareerType type) {
        return getCareersByTypeAndInterval(person, type, null);
    }

    public static Set<Career> getCareersByTypeAndInterval(Person person, CareerType type, Interval intersecting) {
        final Set<Career> careers = new HashSet<Career>();
        for (final Career career : person.getAssociatedCareersSet()) {
            if (type == null || type.equals(CareerType.PROFESSIONAL) && career instanceof ProfessionalCareer
                    || type.equals(CareerType.TEACHING) && career instanceof TeachingCareer) {
                if (intersecting == null || career.getInterval().overlaps(intersecting)) {
                    careers.add(career);
                }
            }
        }
        return careers;
    }

}
