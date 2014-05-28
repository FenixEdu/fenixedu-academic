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
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ExternalTeacherAuthorization;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.TeacherAuthorization;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("professorship")
public class ProfessorshipsGroup extends FenixGroup {
    private static final long serialVersionUID = -6043352087768539757L;

    @GroupArgument("external")
    private Boolean externalAuthorizations;

    @GroupArgument
    private AcademicPeriod period;

    private ProfessorshipsGroup() {
        super();
    }

    private ProfessorshipsGroup(Boolean externalAuthorizations, AcademicPeriod period) {
        this();
        this.externalAuthorizations = externalAuthorizations;
        this.period = period;
    }

    public static ProfessorshipsGroup get(Boolean externalAuthorizations, AcademicPeriod period) {
        return new ProfessorshipsGroup(externalAuthorizations, period);
    }

    @Override
    public String getPresentationNameKey() {
        if (externalAuthorizations) {
            return super.getPresentationNameKey() + ".external";
        }
        return super.getPresentationNameKey();
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { period.getName() };
    }

    @Override
    public Set<User> getMembers() {
        return getMembers(DateTime.now());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        Set<User> users = new HashSet<>();
        //TODO: select active 'when'
        ExecutionInterval interval = ExecutionInterval.getExecutionInterval(AcademicInterval.readDefaultAcademicInterval(period));
        if (interval instanceof ExecutionSemester) {
            ExecutionSemester semester = (ExecutionSemester) interval;
            fillMembers(users, semester);
        } else if (interval instanceof ExecutionYear) {
            for (ExecutionSemester semester : ((ExecutionYear) interval).getExecutionPeriodsSet()) {
                fillMembers(users, semester);
            }
        }
        return users;
    }

    private void fillMembers(Set<User> users, ExecutionSemester semester) {
        if (externalAuthorizations) {
            for (TeacherAuthorization authorization : semester.getAuthorizationSet()) {
                if (authorization instanceof ExternalTeacherAuthorization) {
                    User user = authorization.getTeacher().getPerson().getUser();
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        } else {
            for (final ExecutionCourse executionCourse : semester.getAssociatedExecutionCoursesSet()) {
                for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
                    User user = professorship.getPerson().getUser();
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        }
    }

    @Override
    public boolean isMember(User user) {
        return isMember(user, DateTime.now());
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        if (user == null) {
            return false;
        }
        //TODO: select active 'when'
        AcademicInterval interval = AcademicInterval.readDefaultAcademicInterval(period);
        if (externalAuthorizations) {
            for (ExternalTeacherAuthorization authorization : user.getPerson().getTeacherAuthorizationsAuthorizedSet()) {
                final ExternalTeacherAuthorization externalAuthorization = authorization;
                if (interval.contains(externalAuthorization.getExecutionSemester().getAcademicInterval())) {
                    return true;
                }
            }
        } else {
            for (final Professorship professorship : user.getPerson().getProfessorshipsSet()) {
                final ExecutionCourse executionCourse = professorship.getExecutionCourse();
                if (interval.contains(executionCourse.getAcademicInterval())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentProfessorshipsGroup.getInstance(externalAuthorizations, period);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ProfessorshipsGroup) {
            ProfessorshipsGroup other = (ProfessorshipsGroup) object;
            return Objects.equal(externalAuthorizations, other.externalAuthorizations) && Objects.equal(period, other.period);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(externalAuthorizations, period);
    }
}
