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
 * Department.java
 *
 * Created on 6 de Novembro de 2002, 15:57
 */

/**
 * @author Nuno Nunes & Joana Mota
 */

package org.fenixedu.academic.domain;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.messaging.DepartmentForum;
import org.fenixedu.academic.domain.organizationalStructure.CompetenceCourseGroupUnit;
import org.fenixedu.academic.domain.organizationalStructure.DepartmentUnit;
import org.fenixedu.academic.domain.organizationalStructure.ScientificAreaUnit;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.commons.i18n.LocalizedString;

public class Department extends Department_Base {

    public static final Comparator<Department> COMPARATOR_BY_NAME = new ComparatorChain();
    static {
        ((ComparatorChain) COMPARATOR_BY_NAME).addComparator(new BeanComparator("name", Collator.getInstance()));
        ((ComparatorChain) COMPARATOR_BY_NAME).addComparator(DomainObjectUtil.COMPARATOR_BY_ID);
    }

    public Department() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public List<Teacher> getAllCurrentTeachers() {
        return getTeacherAuthorizationStream().filter(a -> a.getExecutionInterval().isCurrent())
                .map(TeacherAuthorization::getTeacher).distinct().collect(Collectors.toList());
    }

    public List<Teacher> getAllTeachers(AcademicInterval interval) {
        return getTeacherAuthorizationStream().filter(a -> a.getExecutionInterval().getAcademicInterval().overlaps(interval))
                .map(TeacherAuthorization::getTeacher).distinct().collect(Collectors.toList());
    }

    public List<Teacher> getAllTeachers(ExecutionInterval interval) {
        return getTeacherAuthorizationStream().filter(a -> a.getExecutionInterval().equals(interval))
                .map(TeacherAuthorization::getTeacher).distinct().collect(Collectors.toList());
    }

    public List<Teacher> getAllTeachers(ExecutionYear executionYear) {
        return getTeacherAuthorizationStream().filter(a -> a.getExecutionInterval().getExecutionYear().equals(executionYear))
                .map(TeacherAuthorization::getTeacher).distinct().collect(Collectors.toList());
    }

    public List<Teacher> getAllTeachers() {
        return getTeacherAuthorizationStream().map(TeacherAuthorization::getTeacher).distinct().collect(Collectors.toList());
    }

    public Stream<TeacherAuthorization> getRevokedTeacherAuthorizationStream() {
        return getRevokedTeacherAuthorizationSet().stream();
    }

    public Stream<TeacherAuthorization> getTeacherAuthorizationStream() {
        return getTeacherAuthorizationSet().stream();
    }

    public Set<DegreeType> getDegreeTypes() {
        Set<DegreeType> degreeTypes = new TreeSet<DegreeType>();
        for (Degree degree : getDegreesSet()) {
            degreeTypes.add(degree.getDegreeType());
        }
        return degreeTypes;
    }

    public Set<CycleType> getCycleTypes() {
        TreeSet<CycleType> cycles = new TreeSet<CycleType>();
        for (DegreeType degreeType : getDegreeTypes()) {
            cycles.addAll(degreeType.getCycleTypes());
        }
        return cycles;
    }

    public String getAcronym() {
        return StringUtils.isNotBlank(getCode()) ? getCode() : WordUtils.initials(getName()).replaceAll("[a-z]", "");
    }

//    public List<CompetenceCourse> getBolonhaCompetenceCourses() {
//        DepartmentUnit departmentUnit = this.getDepartmentUnit();
//        List<CompetenceCourse> courses = new ArrayList<CompetenceCourse>();
//        for (ScientificAreaUnit areaUnit : departmentUnit.getScientificAreaUnits()) {
//            for (CompetenceCourseGroupUnit competenceCourseGroupUnit : areaUnit.getCompetenceCourseGroupUnits()) {
//                courses.addAll(competenceCourseGroupUnit.getCompetenceCourses());
//            }
//        }
//        return courses;
//    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------
    public static Department readByName(final String departmentName) {
        for (final Department department : Bennu.getInstance().getDepartmentsSet()) {
            if (department.getName().equals(departmentName)) {
                return department;
            }
        }
        return null;
    }

    public void delete() {
        if (!getTeacherGroupSet().isEmpty()) {
            throw new DomainException("error.department.cannotDeleteDepartmentUsedInAccessControl");
        }
        setMembersGroup(null);
        setDepartmentUnit(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    /**
     * Joins the portuguese and english version of the department's name in a
     * LocalizedString for an easier handling of the name in a
     * internacionalized context.
     *
     * @return a LocalizedString with the portuguese and english versions of
     *         the department's name
     */
    public LocalizedString getNameI18n() {
        return new LocalizedString().with(org.fenixedu.academic.util.LocaleUtils.PT, getRealName())
                .with(org.fenixedu.academic.util.LocaleUtils.EN, getRealNameEn());
    }

    public Integer getCompetenceCourseInformationChangeRequestsCount() {
        int count = 0;
        for (CompetenceCourse course : CompetenceCourse.findByUnit(getDepartmentUnit(), true).collect(Collectors.toSet())) {
            count += course.getCompetenceCourseInformationChangeRequestsSet().size();
        }

        return count;
    }

    public Integer getDraftCompetenceCourseInformationChangeRequestsCount() {
        int count = 0;
        for (CompetenceCourse course : CompetenceCourse.findByUnit(getDepartmentUnit(), true).collect(Collectors.toSet())) {
            count += course.getDraftCompetenceCourseInformationChangeRequestsCount();
        }

        return count;
    }

    public Group getCompetenceCourseMembersGroup() {
        return getMembersGroup().toGroup();
    }

    public void setCompetenceCourseMembersGroup(Group group) {
        setMembersGroup(group.toPersistentGroup());
    }

    public boolean isUserMemberOfCompetenceCourseMembersGroup(User user) {
        return getCompetenceCourseMembersGroup().isMember(user);
    }

    public boolean isUserMemberOfCompetenceCourseMembersGroup(Person person) {
        return getCompetenceCourseMembersGroup().isMember(person.getUser());
    }

    public boolean isCurrentUserMemberOfCompetenceCourseMembersGroup() {
        return isUserMemberOfCompetenceCourseMembersGroup(AccessControl.getPerson());
    }

    public DepartmentForum getDepartmentForum() {
        return getForum();
    }

    public static Department find(final String departmentCode) {
        for (final Department department : Bennu.getInstance().getDepartmentsSet()) {
            if (department.getAcronym().equals(departmentCode)) {
                return department;
            }
        }
        if (StringUtils.isNumeric(departmentCode)) {
            final Unit unit = Unit.readByCostCenterCode(new Integer(departmentCode));
            if (unit != null) {
                final org.fenixedu.academic.domain.organizationalStructure.DepartmentUnit departmentUnit =
                        unit.getAssociatedDepartmentUnit();
                return departmentUnit == null ? null : departmentUnit.getDepartment();
            }
        }
        return null;
    }

    public static List<Department> readActiveDepartments() {
        return Bennu.getInstance().getDepartmentsSet().stream().filter(Department::getActive)
                .sorted(Department.COMPARATOR_BY_NAME).collect(Collectors.toList());

//        final List<Department> departments = new ArrayList<Department>();
//        for (final Department department : Bennu.getInstance().getDepartmentsSet()) {
//            if (department.getActive()) {
//                departments.add(department);
//            }
//        }
//        Collections.sort(departments, Department.COMPARATOR_BY_NAME);
//        return departments;
    }

}
