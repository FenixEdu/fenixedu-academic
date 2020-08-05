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

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.person.RoleType;
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
    private ExecutionYear executionYear;

    private TeacherGroup() {
        super();
    }

    private TeacherGroup(Degree degree, ExecutionCourse executionCourse, Space campus, ExecutionYear executionYear) {
        this();
        this.degree = degree;
        this.executionCourse = executionCourse;
        this.campus = campus;
        this.executionYear = executionYear;
    }

    public static TeacherGroup get(Degree degree) {
        return new TeacherGroup(degree, null, null, null);
    }

    public static TeacherGroup get(Space campus) {
        return new TeacherGroup(null, null, campus, null);
    }

    public static TeacherGroup get(ExecutionCourse executionCourse) {
        return new TeacherGroup(null, executionCourse, null, null);
    }

    public static TeacherGroup get(Degree degree, ExecutionCourse executionCourse, Space campus, ExecutionYear executionYear) {
        return new TeacherGroup(degree, executionCourse, campus, executionYear);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        List<String> parts = new ArrayList<>();
        String connector = "";
        if (degree != null) {
            parts.add(degree.getPresentationName());
        }
        if (executionCourse != null) {
            parts.add(executionCourse.getName() + " (" + executionCourse.getSigla() + ") "
                    + executionCourse.getAcademicInterval().getPathName());
        }
        if (executionYear != null) {
            parts.add(executionYear.getName());
        }
        if (campus != null) {
            parts.add(campus.getName());
        }
        if (!parts.isEmpty()) {
            if (parts.size() == 1 && campus != null) {
                //campus is first
                connector = BundleUtil.getString(Bundle.GROUP, "label.name.connector.campus");
            } else if (degree == null && executionCourse == null) {
                //department is first
                connector = BundleUtil.getString(Bundle.GROUP, "label.name.connector.male");
            } else {
                connector = BundleUtil.getString(Bundle.GROUP, "label.name.connector.default");
            }
        }
        return new String[] { connector, Joiner.on(", ").join(parts) };
    }

    @Override
    public Stream<User> getMembers() {
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
            RoleType.TEACHER.actualGroup().getMembers().forEach(user -> {
                if (user.getPerson() != null && user.getPerson().getTeacher().teachesAt(campus)) {
                    users.add(user);
                }
            });
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
        return users.stream();
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
        }
        return false;
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentTeacherGroup.getInstance(degree, executionCourse, campus, executionYear);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof TeacherGroup) {
            TeacherGroup other = (TeacherGroup) object;
            return Objects.equal(degree, other.degree) && Objects.equal(executionCourse, other.executionCourse)
                    && Objects.equal(campus, other.campus) && Objects.equal(executionYear, other.executionYear);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(degree, executionCourse, campus, executionYear);
    }
}
