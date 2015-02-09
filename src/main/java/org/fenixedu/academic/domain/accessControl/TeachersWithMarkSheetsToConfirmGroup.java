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

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.MarkSheet;
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
@GroupOperator("teachersWithMarkSheetsToConfirm")
public class TeachersWithMarkSheetsToConfirmGroup extends FenixGroup {
    private static final long serialVersionUID = -6490147275585620321L;

    @GroupArgument
    private ExecutionSemester period;

    @GroupArgument
    private DegreeCurricularPlan degreeCurricularPlan;

    private TeachersWithMarkSheetsToConfirmGroup() {
        super();
    }

    private TeachersWithMarkSheetsToConfirmGroup(ExecutionSemester period, DegreeCurricularPlan degreeCurricularPlan) {
        this();
        this.period = period;
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public static TeachersWithMarkSheetsToConfirmGroup get(ExecutionSemester period, DegreeCurricularPlan degreeCurricularPlan) {
        return new TeachersWithMarkSheetsToConfirmGroup(period, degreeCurricularPlan);
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
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (MarkSheet markSheet : period.getMarkSheetsToConfirm(degreeCurricularPlan)) {
            if (markSheet.getResponsibleTeacher().getPerson() != null) {
                User user = markSheet.getResponsibleTeacher().getPerson().getUser();
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
        return user != null && getMembers().contains(user);
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentTeachersWithMarkSheetsToConfirmGroup.getInstance(period, degreeCurricularPlan);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof TeachersWithMarkSheetsToConfirmGroup) {
            TeachersWithMarkSheetsToConfirmGroup other = (TeachersWithMarkSheetsToConfirmGroup) object;
            return Objects.equal(period, other.period) && Objects.equal(degreeCurricularPlan, other.degreeCurricularPlan);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(period, degreeCurricularPlan);
    }
}
