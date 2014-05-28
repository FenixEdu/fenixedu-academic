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
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
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
        if (degreeType != null) {
            parts.add(degreeType.getFilteredName());
        }
        if (degree != null) {
            parts.add(degree.getNameI18N().getContent());
        }
        if (cycle != null) {
            parts.add(cycle.getDescription());
        }
        if (campus != null) {
            parts.add(campus.getName());
        }

        if (executionCourse != null) {
            parts.add(executionCourse.getName());
        }
        if (curricularYear != null) {
            parts.add(curricularYear.getYear().toString());
        }
        if (executionYear != null) {
            parts.add(executionYear.getName());
        }
        return new String[] { Joiner.on(", ").join(parts) };
    }

    private ExecutionYear getExecutionYear() {
        return executionYear != null ? executionYear : ExecutionYear.readCurrentExecutionYear();
    }

    @Override
    public Set<User> getMembers() {
        if (executionCourse != null) {
            if (degree == null && degreeType == null && campus == null) {
                return registrationsToUsers(getCourseBasedRegistrations(executionCourse));
            } else {
                return registrationsToUsers(Sets.intersection(getCourseBasedRegistrations(executionCourse),
                        getDegreeBasedRegistrations()));
            }
        } else if (campus != null) {
            return registrationsToUsers(getCampusBasedRegistrations());
        } else {
            return registrationsToUsers(getDegreeBasedRegistrations());
        }
    }

    private Set<Registration> getDegreeBasedRegistrations() {
        FluentIterable<Registration> registrations;
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
        return registrations.toSet();
    }

    private FluentIterable<Registration> filterDegreeType(FluentIterable<Registration> registrations, final DegreeType degreeType) {
        if (degreeType == null) {
            return registrations;
        }
        return registrations.filter(new Predicate<Registration>() {
            @Override
            public boolean apply(Registration registration) {
                return registration.getDegreeType().equals(degreeType);
            }
        });
    }

    private FluentIterable<Registration> filterDegree(FluentIterable<Registration> registrations, final Degree degree) {
        if (degree == null) {
            return registrations;
        }
        return registrations.filter(new Predicate<Registration>() {
            @Override
            public boolean apply(Registration registration) {
                if (registration.getActiveStudentCurricularPlan() != null) {
                    return registration.getActiveStudentCurricularPlan().getDegree().equals(degree);
                }
                return false;
            }
        });
    }

    private static FluentIterable<Registration> filterCycle(FluentIterable<Registration> registrations,
            final CycleType cycleType, final ExecutionYear executionYear) {
        if (cycleType == null) {
            return registrations;
        }
        return registrations.filter(new Predicate<Registration>() {
            @Override
            public boolean apply(Registration registration) {
                return Objects.equal(registration.getCycleType(executionYear), cycleType);
            }
        });
    }

    private static FluentIterable<Registration> filterCurricularYear(FluentIterable<Registration> registrations,
            final CurricularYear curricularYear, final ExecutionYear executionYear) {
        if (curricularYear == null) {
            return registrations;
        }
        return registrations.filter(new Predicate<Registration>() {
            @Override
            public boolean apply(Registration registration) {
                return registration.getCurricularYear(executionYear) == curricularYear.getYear();
            }
        });
    }

    private static FluentIterable<Registration> getRegistrations(DegreeType type) {
        Set<Registration> registrations = new HashSet<>();
        for (Person person : Role.getRoleByRoleType(RoleType.STUDENT).getAssociatedPersonsSet()) {
            Registration registration = person.getStudentByType(type);
            if (registration != null && registration.isActive()) {
                registrations.add(registration);
            }
        }
        return FluentIterable.from(registrations);
    }

    private static FluentIterable<Registration> getRegistrations(Degree degree) {
        Set<Registration> registrations = new HashSet<>();
        for (DegreeCurricularPlan degreeCurricularPlan : degree.getActiveDegreeCurricularPlans()) {
            for (StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlansSet()) {
                if (studentCurricularPlan.isActive()) {
                    registrations.add(studentCurricularPlan.getRegistration());
                }
            }
        }
        return FluentIterable.from(registrations);
    }

    private static FluentIterable<Registration> getRegistrations() {
        Set<Registration> registrations = new HashSet<>();
        for (Person person : Role.getRoleByRoleType(RoleType.STUDENT).getAssociatedPersonsSet()) {
            registrations.addAll(person.getStudent().getActiveRegistrations());
        }
        return FluentIterable.from(registrations);
    }

    private static Set<User> registrationsToUsers(Set<Registration> registrations) {
        Set<User> users = new HashSet<>();
        for (Registration registration : registrations) {
            User user = registration.getPerson().getUser();
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }

    private static Set<Registration> getCourseBasedRegistrations(ExecutionCourse executionCourse) {
        Set<Registration> registrations = new HashSet<>();
        for (Attends attends : executionCourse.getAttendsSet()) {
            registrations.add(attends.getRegistration());
        }
        return registrations;
    }

    private Set<Registration> getCampusBasedRegistrations() {
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
        return registrations;
    }

    @Override
    public Set<User> getMembers(DateTime when) {
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
            if (registration.isActive()) {
                if (degreeType != null && registration.getDegree().getDegreeType() != degreeType) {
                    continue;
                }
                if (degree != null && registration.getActiveStudentCurricularPlan() != null
                        && !registration.getActiveStudentCurricularPlan().getDegree().equals(degree)) {
                    continue;
                }
                if (cycle != null && registration.getCurrentCycleType() != cycle) {
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
