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
import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

/**
 * @deprecated pointless group used via programmatic API to send email, never seen by a user its main purpose is to create
 *             hundreds of objects in the database just to send a notification to delayed teachers.
 */
@Deprecated
@GroupOperator("teachersWithGradesToSubmit")
public class TeachersWithGradesToSubmitGroup extends FenixGroup {
    private static final long serialVersionUID = 1231342910537087397L;

    @GroupArgument
    private ExecutionSemester period;

    @GroupArgument
    private DegreeCurricularPlan degreeCurricularPlan;

    private TeachersWithGradesToSubmitGroup() {
        super();
    }

    private TeachersWithGradesToSubmitGroup(ExecutionSemester period, DegreeCurricularPlan degreeCurricularPlan) {
        this();
        this.period = period;
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public static TeachersWithGradesToSubmitGroup get(ExecutionSemester period, DegreeCurricularPlan degreeCurricularPlan) {
        return new TeachersWithGradesToSubmitGroup(period, degreeCurricularPlan);
    }

    @Override
    public String getPresentationNameKey() {
        if (degreeCurricularPlan == null) {
            return super.getPresentationNameKey() + ".allDegrees";
        }
        return super.getPresentationNameKey();
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        if (degreeCurricularPlan == null) {
            return new String[] { period.getName() };
        }
        return new String[] { degreeCurricularPlan.getPresentationName(), period.getName() };
    }

    @Override
    public Stream<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (ExecutionCourse executionCourse : period.getExecutionCoursesWithDegreeGradesToSubmit(degreeCurricularPlan)) {
            for (Professorship professorship : executionCourse.getProfessorshipsSet()) {
                if (professorship.getResponsibleFor()) {
                    if (professorship.getPerson() != null) {
                        User user = professorship.getPerson().getUser();
                        if (user != null) {
                            users.add(user);
                        }
                    }
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
        return user != null && getMembers().anyMatch(u -> u == user);
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentTeachersWithGradesToSubmitGroup.getInstance(period, degreeCurricularPlan);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof TeachersWithGradesToSubmitGroup) {
            TeachersWithGradesToSubmitGroup other = (TeachersWithGradesToSubmitGroup) object;
            return Objects.equal(period, other.period) && Objects.equal(degreeCurricularPlan, other.degreeCurricularPlan);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(period, degreeCurricularPlan);
    }
}
