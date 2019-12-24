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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;
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
        return new String[] { BundleUtil.getString(Bundle.ENUMERATION, period.getName()) };
    }

    @Override
    public Stream<User> getMembers() {
        return getMembers(DateTime.now());
    }

    @Override
    public Stream<User> getMembers(DateTime when) {
        Set<User> users = new HashSet<>();
        //TODO: select active 'when'
        ExecutionInterval interval = ExecutionInterval.getExecutionInterval(AcademicInterval.readDefaultAcademicInterval(period));
        if (interval instanceof ExecutionYear) {
            for (ExecutionInterval childInterval : ((ExecutionYear) interval).getExecutionPeriodsSet()) {
                fillMembers(users, childInterval);
            }
        } else  {
            fillMembers(users, interval);
        }
        return users.stream();
    }

    private void fillMembers(Set<User> users, ExecutionInterval interval) {
        if (externalAuthorizations) {
            users.addAll(interval.getTeacherAuthorizationSet().stream().filter(a -> !a.isContracted())
                    .map(a -> a.getTeacher().getPerson().getUser()).collect(Collectors.toSet()));
        } else {
            for (final ExecutionCourse executionCourse : interval.getAssociatedExecutionCoursesSet()) {
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
            if (user.getPerson().getTeacher().getTeacherAuthorization(interval).isPresent()) {
                return true;
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
