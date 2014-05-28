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

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("teacher")
public class TeacherGroup extends FenixGroup {
    private static final long serialVersionUID = 4333469299499201648L;

    @GroupArgument
    private Degree degree;

    @GroupArgument
    private ExecutionCourse executionCourse;

    @GroupArgument
    private Space campus;

    @GroupArgument
    private Department department;

    @GroupArgument
    private ExecutionYear executionYear;

    private TeacherGroup() {
        super();
    }

    private TeacherGroup(Degree degree, ExecutionCourse executionCourse, Space campus, Department department,
            ExecutionYear executionYear) {
        this();
        this.degree = degree;
        this.executionCourse = executionCourse;
        this.campus = campus;
        this.department = department;
        this.executionYear = executionYear;
    }

    public static TeacherGroup get(Degree degree) {
        return new TeacherGroup(degree, null, null, null, null);
    }

    public static TeacherGroup get(Space campus) {
        return new TeacherGroup(null, null, campus, null, null);
    }

    public static TeacherGroup get(Department department, ExecutionYear executionYear) {
        return new TeacherGroup(null, null, null, department, executionYear);
    }

    public static TeacherGroup get(ExecutionCourse executionCourse) {
        return new TeacherGroup(null, executionCourse, null, null, null);
    }

    public static TeacherGroup get(Degree degree, ExecutionCourse executionCourse, Space campus, Department department,
            ExecutionYear executionYear) {
        return new TeacherGroup(degree, executionCourse, campus, department, executionYear);
    }

    private ExecutionYear getExecutionYear() {
        return executionYear != null ? executionYear : ExecutionYear.readCurrentExecutionYear();
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        if (degree != null) {
            return new String[] { degree.getName() };
        } else if (campus != null) {
            return new String[] { campus.getName() };
        } else if (department != null) {
            return new String[] { department.getName() };
        } else if (executionCourse != null) {
            return new String[] { executionCourse.getName() };
        }
        return new String[0];
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();

        //by degree
        if (degree != null) {
            for (DegreeCurricularPlan degreeCurricularPlan : degree.getActiveDegreeCurricularPlans()) {
                for (CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
                    for (ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
                        if (executionCourse.getExecutionYear().isCurrent()) {
                            for (Professorship professorship : executionCourse.getProfessorshipsSet()) {
                                User user = professorship.getPerson().getUser();
                                if (user != null) {
                                    users.add(user);
                                }
                            }
                        }
                    }
                }
            }
        }
        //by campus
        if (campus != null) {
            for (Person person : Role.getRoleByRoleType(RoleType.TEACHER).getAssociatedPersonsSet()) {
                if (person.getTeacher().teachesAt(campus)) {
                    User user = person.getUser();
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        }
        //by department
        if (department != null) {
            for (Teacher teacher : department.getAllTeachers(getExecutionYear().getBeginDateYearMonthDay(), getExecutionYear()
                    .getEndDateYearMonthDay())) {
                User user = teacher.getPerson().getUser();
                if (user != null) {
                    users.add(user);
                }
            }
        }
        //by execution course
        if (executionCourse != null) {
            for (Professorship professorship : executionCourse.getProfessorshipsSet()) {
                User user = professorship.getPerson().getUser();
                if (user != null) {
                    users.add(user);
                }
            }
        }
        return users;
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
        if (user.getPerson().getTeacher() != null) {
            for (final Professorship professorship : user.getPerson().getTeacher().getProfessorshipsSet()) {
                ExecutionCourse course = professorship.getExecutionCourse();
                if (degree != null) {
                    for (final CurricularCourse curricularCourse : course.getAssociatedCurricularCoursesSet()) {
                        if (curricularCourse.getDegree().equals(degree)) {
                            return true;
                        }
                    }
                }
                if (executionCourse != null) {
                    if (course.equals(executionCourse)) {
                        return true;
                    }
                }
                if (campus != null) {
                    if (course.functionsAt(campus)) {
                        return true;
                    }
                }
            }
            if (department != null) {
                if (department.equals(user
                        .getPerson()
                        .getTeacher()
                        .getLastWorkingDepartment(getExecutionYear().getBeginDateYearMonthDay(),
                                getExecutionYear().getEndDateYearMonthDay()))) {
                    return true;
                }
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
        return PersistentTeacherGroup.getInstance(degree, executionCourse, campus, department, executionYear);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof TeacherGroup) {
            TeacherGroup other = (TeacherGroup) object;
            return Objects.equal(degree, other.degree) && Objects.equal(executionCourse, other.executionCourse)
                    && Objects.equal(campus, other.campus) && Objects.equal(department, other.department)
                    && Objects.equal(executionYear, other.executionYear);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(degree, executionCourse, campus, department, executionYear);
    }
}
