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
 * Created on Apr 27, 2005
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class NonAffiliatedTeacher extends NonAffiliatedTeacher_Base {

    public NonAffiliatedTeacher() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public NonAffiliatedTeacher(final String name, final Unit institution) {
        this();
        setName(name);
        setInstitutionUnit(institution);
    }

    public static Set<NonAffiliatedTeacher> findNonAffiliatedTeacherByName(final String name) {
        Pattern pattern = Pattern.compile(name.toLowerCase());
        final Set<NonAffiliatedTeacher> nonAffiliatedTeachers = new HashSet<NonAffiliatedTeacher>();
        for (final NonAffiliatedTeacher nonAffiliatedTeacher : Bennu.getInstance().getNonAffiliatedTeachersSet()) {
            Matcher matcher = pattern.matcher(nonAffiliatedTeacher.getName().toLowerCase());
            if (matcher.find()) {
                nonAffiliatedTeachers.add(nonAffiliatedTeacher);
            }
        }
        return nonAffiliatedTeachers;
    }

    @Atomic
    public static void associateToInstitutionAndExecutionCourse(final String nonAffiliatedTeacherName, final Unit institution,
            final ExecutionCourse executionCourse) {

        NonAffiliatedTeacher nonAffiliatedTeacher = institution.findNonAffiliatedTeacherByName(nonAffiliatedTeacherName);
        if (nonAffiliatedTeacher == null) {
            nonAffiliatedTeacher = new NonAffiliatedTeacher(nonAffiliatedTeacherName, institution);
        }

        if (nonAffiliatedTeacher.getExecutionCourses().contains(executionCourse)) {
            throw new DomainException("error.invalid.executionCourse");
        } else {
            nonAffiliatedTeacher.addExecutionCourses(executionCourse);
        }

    }

    @Atomic
    public void removeExecutionCourse(final ExecutionCourse executionCourse) {
        getExecutionCourses().remove(executionCourse);
    }

    public void delete() {

        if (hasAnyAssociatedInquiriesTeachers()) {
            throw new DomainException("error.NonAffiliatedTeacher.hasAnyAssociatedInquiriesTeachers",
                    Unit.getInstitutionAcronym());
        }

        setRootDomainObject(null);
        setInstitutionUnit(null);
        getExecutionCourses().clear();

        super.deleteDomainObject();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExecutionCourse> getExecutionCourses() {
        return getExecutionCoursesSet();
    }

    @Deprecated
    public boolean hasAnyExecutionCourses() {
        return !getExecutionCoursesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.InquiriesTeacher> getAssociatedInquiriesTeachers() {
        return getAssociatedInquiriesTeachersSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedInquiriesTeachers() {
        return !getAssociatedInquiriesTeachersSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasInstitutionUnit() {
        return getInstitutionUnit() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

}
