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
package org.fenixedu.academic.domain.accessControl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule.ConclusionValue;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;

@GroupOperator("student")
public class StudentGroup extends FenixGroup {
    private static final long serialVersionUID = -3059659317143315425L;

    @GroupArgument
    private DegreeType degreeType;

    @GroupArgument
    private Degree degree;

    @GroupArgument
    private CycleType cycle;

    @GroupArgument
    private Space campus;

    @GroupArgument
    private ExecutionCourse executionCourse;

    @GroupArgument
    private CurricularYear curricularYear;

    @GroupArgument
    private ExecutionYear executionYear;

    private StudentGroup() {
        super();
    }

    private StudentGroup(DegreeType degreeType, Degree degree, CycleType cycle, Space campus, ExecutionCourse executionCourse,
            CurricularYear curricularYear, ExecutionYear executionYear) {
        super();
        this.degreeType = degreeType;
        this.degree = degree;
        this.cycle = cycle;
        this.campus = campus;
        this.executionCourse = executionCourse;
        this.curricularYear = curricularYear;
        this.executionYear = executionYear;
    }

    public static StudentGroup get() {
        return new StudentGroup(null, null, null, null, null, null, null);
    }

    public static StudentGroup get(DegreeType degreeType) {
        return new StudentGroup(degreeType, null, null, null, null, null, null);
    }

    public static StudentGroup get(Degree degree, CycleType cycle) {
        return new StudentGroup(null, degree, cycle, null, null, null, null);
    }

    public static StudentGroup get(CycleType cycle) {
        return new StudentGroup(null, null, cycle, null, null, null, null);
    }

    public static StudentGroup get(CycleType cycle, ExecutionYear executionYear) {
        return new StudentGroup(null, null, cycle, null, null, null, executionYear);
    }

    public static StudentGroup get(Space campus) {
        return new StudentGroup(null, null, null, campus, null, null, null);
    }

    public static StudentGroup get(ExecutionCourse executionCourse) {
        return new StudentGroup(null, null, null, null, executionCourse, null, null);
    }

    public static StudentGroup get(Degree degree, CurricularYear curricularYear, ExecutionYear executionYear) {
        return new StudentGroup(null, degree, null, null, null, curricularYear, executionYear);
    }

    public static StudentGroup get(DegreeType degreeType, Degree degree, CycleType cycle, Space campus,
            ExecutionCourse executionCourse, CurricularYear curricularYear, ExecutionYear executionYear) {
        return new StudentGroup(degreeType, degree, cycle, campus, executionCourse, curricularYear, executionYear);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        List<String> parts = new ArrayList<>();
        String connector = "";
        if (degreeType != null) {
            parts.add(degreeType.getName().getContent());
        }
        if (degree != null) {
            parts.add(degree.getPresentationName());
        }
        if (executionCourse != null) {
            parts.add(executionCourse.getName() + " (" + executionCourse.getSigla() + ") "
                    + executionCourse.getAcademicInterval().getPathName());
        }
        if (cycle != null) {
            parts.add(cycle.getDescription());
        }
        if (curricularYear != null) {
            parts.add(curricularYear.getYear().toString());
        }
        if (executionYear != null) {
            parts.add(executionYear.getName());
        }
        if (campus != null) {
            parts.add(campus.getName());
        }
        if (!parts.isEmpty()) {
            if (parts.size() == 1 && campus != null) {
                connector = BundleUtil.getString(Bundle.GROUP, "label.name.connector.campus");
            } else {
                connector = BundleUtil.getString(Bundle.GROUP, "label.name.connector.default");
            }
        }
        return new String[] { connector, Joiner.on(", ").join(parts) };
    }

    private ExecutionYear getExecutionYear() {
        return executionYear != null ? executionYear : ExecutionYear.readCurrentExecutionYear();
    }

    @Override
    public Stream<User> getMembers() {
        if (executionCourse != null) {
            if (degree == null && degreeType == null && campus == null) {
                return registrationsToUsers(getCourseBasedRegistrations(executionCourse).stream());
            } else {
                return registrationsToUsers(Sets.intersection(getCourseBasedRegistrations(executionCourse),
                        getDegreeBasedRegistrations()).stream());
            }
        } else if (campus != null) {
            return registrationsToUsers(getCampusBasedRegistrations());
        } else {
            return registrationsToUsers(getDegreeBasedRegistrations().stream());
        }
    }

    private Set<Registration> getDegreeBasedRegistrations() {
        Stream<Registration> registrations;
        if (degreeType != null) {
            registrations = getRegistrations(degreeType);
        } else if (degree != null) {
            registrations = getRegistrations(degree);
        } else {
            registrations = getRegistrations();
        }
        registrations = filterDegreeType(registrations, degreeType);
        registrations = filterDegree(registrations, degree);
        registrations = filterCycle(registrations, cycle, getExecutionYear());
        registrations = filterCurricularYear(registrations, curricularYear, getExecutionYear());
        return registrations.collect(Collectors.toSet());
    }

    private Stream<Registration> filterDegreeType(Stream<Registration> registrations, final DegreeType degreeType) {
        if (degreeType == null) {
            return registrations;
        }
        return registrations.filter(registration -> registration.getDegreeType().equals(degreeType));
    }

    private Stream<Registration> filterDegree(Stream<Registration> registrations, final Degree degree) {
        if (degree == null) {
            return registrations;
        }
        return registrations.filter(registration -> {
            if (registration.getActiveStudentCurricularPlan() != null) {
                return registration.getActiveStudentCurricularPlan().getDegree().equals(degree);
            }
            return false;
        });
    }

    private static Stream<Registration> filterCycle(Stream<Registration> registrations,
            final CycleType cycleType, final ExecutionYear executionYear) {
        if (cycleType == null) {
            return registrations;
        }
        return registrations
                .filter(registration -> isActiveRegistrationsWithAtLeastOneEnrolment(registration, cycleType, executionYear));
    }

    private static boolean isActiveRegistrationsWithAtLeastOneEnrolment(Registration registration, final CycleType cycleType,
            final ExecutionYear executionYear) {
        StudentCurricularPlan studentCurricularPlan = registration.getStudentCurricularPlan(executionYear);
        if (studentCurricularPlan != null && !studentCurricularPlan.hasConcludedCycle(cycleType, executionYear)) {
            CycleCurriculumGroup cycleCurriculumGroup = studentCurricularPlan.getCycle(cycleType);
            if (cycleCurriculumGroup != null
                    && cycleCurriculumGroup.isConcluded(executionYear).equals(ConclusionValue.NOT_CONCLUDED)
                    && cycleCurriculumGroup.hasAnyEnrolments()) {
                return true;
            }
        }
        return false;
    }

    private static Stream<Registration> filterCurricularYear(Stream<Registration> registrations,
            final CurricularYear curricularYear, final ExecutionYear executionYear) {
        if (curricularYear == null) {
            return registrations;
        }
        return registrations.filter(registration -> registration.getCurricularYear(executionYear) == curricularYear.getYear());
    }

    private static Stream<Registration> getRegistrations(DegreeType type) {
        Set<Registration> registrations = new HashSet<>();
        RoleType.STUDENT.actualGroup().getMembers().forEach(user -> {
            user.getPerson().getStudentsSet().forEach(reg -> {
                if (reg.getDegreeType() == type && reg.isActive()) {
                    registrations.add(reg);
                }
            });
        });
        return registrations.stream();
    }

    private static Stream<Registration> getRegistrations(Degree degree) {
        Set<Registration> registrations = new HashSet<>();
        for (DegreeCurricularPlan degreeCurricularPlan : degree.getActiveDegreeCurricularPlans()) {
            for (StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlansSet()) {
                if (studentCurricularPlan.isActive()) {
                    registrations.add(studentCurricularPlan.getRegistration());
                }
            }
        }
        return registrations.stream();
    }

    private static Stream<Registration> getRegistrations() {
        Set<Registration> registrations = new HashSet<>();
        RoleType.STUDENT.actualGroup().getMembers().forEach(user -> {
            registrations.addAll(user.getPerson().getStudent().getActiveRegistrations());
        });
        return registrations.stream();
    }

    private static Stream<User> registrationsToUsers(Stream<Registration> registrations) {
        return registrations.map(reg -> reg.getPerson().getUser()).filter(o -> o != null);
    }

    private static Set<Registration> getCourseBasedRegistrations(ExecutionCourse executionCourse) {
        Set<Registration> registrations = new HashSet<>();
        for (Attends attends : executionCourse.getAttendsSet()) {
            registrations.add(attends.getRegistration());
        }
        return registrations;
    }

    private Stream<Registration> getCampusBasedRegistrations() {
        Set<Registration> registrations = new HashSet<>();
        for (final ExecutionDegree executionDegree : ExecutionYear.readCurrentExecutionYear().getExecutionDegreesSet()) {
            if (executionDegree.getCampus().equals(campus)) {
                final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
                for (final StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlansSet()) {
                    final Registration registration = studentCurricularPlan.getRegistration();
                    if (registration != null && registration.isActive()) {
                        registrations.add(registration);
                    }
                }
            }
        }
        return registrations.stream();
    }

    @Override
    public Stream<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        if (user == null) {
            return false;
        }
        if (user.getPerson().getStudent() == null) {
            return false;
        }
        for (final Registration registration : user.getPerson().getStudent().getRegistrationsSet()) {
            if (executionCourse != null && registration.getAttendingExecutionCoursesFor().contains(executionCourse)) {
                return true;
            }
            if (registration.isActive()) {
                if (degreeType != null && registration.getDegree().getDegreeType() != degreeType) {
                    continue;
                }
                if (degree != null && registration.getActiveStudentCurricularPlan() != null
                        && !registration.getActiveStudentCurricularPlan().getDegree().equals(degree)) {
                    continue;
                }
                if (cycle != null && !isActiveRegistrationsWithAtLeastOneEnrolment(registration, cycle, executionYear)) {
                    continue;
                }
                if (campus != null && registration.getCampus() != campus) {
                    continue;
                }
                if (executionCourse != null && !registration.getAttendingExecutionCoursesFor().contains(executionCourse)) {
                    continue;
                }
                if (curricularYear != null && registration.getCurricularYear() != curricularYear.getYear()) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentStudentGroup.getInstance(degreeType, degree, cycle, campus, executionCourse, curricularYear,
                executionYear);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof StudentGroup) {
            StudentGroup other = (StudentGroup) object;
            return Objects.equal(degreeType, other.degreeType) && Objects.equal(degree, other.degree)
                    && Objects.equal(cycle, other.cycle) && Objects.equal(campus, other.campus)
                    && Objects.equal(executionCourse, other.executionCourse)
                    && Objects.equal(curricularYear, other.curricularYear) && Objects.equal(executionYear, other.executionYear);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(degreeType, degree, cycle, campus, executionCourse, curricularYear, executionYear);
    }
}
