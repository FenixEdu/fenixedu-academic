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

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;

import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("responsibleForExecutionCourse")
public class ResponsibleForExecutionCourseGroup extends FenixGroup {
    private static final long serialVersionUID = 1695000768665174854L;

    private static final ResponsibleForExecutionCourseGroup INSTANCE = new ResponsibleForExecutionCourseGroup();

    private ResponsibleForExecutionCourseGroup() {
        super();
    }

    public static ResponsibleForExecutionCourseGroup get() {
        return INSTANCE;
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (final ExecutionYear executionYear : Bennu.getInstance().getExecutionYearsSet()) {
            if (executionYear.isCurrent()) {
                for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriodsSet()) {
                    for (final ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
                        for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
                            if (professorship.getResponsibleFor().booleanValue()) {
                                User user = professorship.getPerson().getUser();
                                if (user != null) {
                                    users.add(user);
                                }
                            }
                        }
                    }
                }
                break;
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
        if (user != null && user.getPerson().getTeacher() != null) {
            for (final Professorship professorship : user.getPerson().getTeacher()
                    .getProfessorships(ExecutionYear.readCurrentExecutionYear())) {
                if (professorship.isResponsibleFor()) {
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
        return PersistentResponsibleForExecutionCourseGroup.getInstance();
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof ResponsibleForExecutionCourseGroup;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ResponsibleForExecutionCourseGroup.class);
    }
}
