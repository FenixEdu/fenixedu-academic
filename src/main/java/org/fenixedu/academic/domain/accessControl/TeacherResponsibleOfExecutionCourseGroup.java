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

import java.util.HashSet;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

@GroupOperator("teacherResponsibleOfExecutionCourse")
public class TeacherResponsibleOfExecutionCourseGroup extends FenixGroup {
    private static final long serialVersionUID = -8474941837282629610L;

    @GroupArgument
    private ExecutionCourse executionCourse;

    private TeacherResponsibleOfExecutionCourseGroup() {
        super();
    }

    private TeacherResponsibleOfExecutionCourseGroup(ExecutionCourse executionCourse) {
        this();
        this.executionCourse = executionCourse;
    }

    public static TeacherResponsibleOfExecutionCourseGroup get(ExecutionCourse executionCourse) {
        return new TeacherResponsibleOfExecutionCourseGroup(executionCourse);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { executionCourse.getNameI18N().getContent() + " (" + executionCourse.getSigla() + ") "
                + executionCourse.getAcademicInterval().getPathName() };
    }

    @Override
    public Stream<User> getMembers() {
        return executionCourse.getProfessorshipsSet().stream().filter(Professorship::getResponsibleFor)
                .map(professorship -> professorship.getPerson().getUser());
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
        if (!Sets.intersection(user.getPerson().getProfessorshipsSet(), new HashSet<>(executionCourse.responsibleFors()))
                .isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentTeacherResponsibleOfExecutionCourseGroup.getInstance(executionCourse);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof TeacherResponsibleOfExecutionCourseGroup) {
            return Objects.equal(executionCourse, ((TeacherResponsibleOfExecutionCourseGroup) object).executionCourse);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(executionCourse);
    }
}
