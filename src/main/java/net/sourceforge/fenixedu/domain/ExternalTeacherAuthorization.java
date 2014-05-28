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
package net.sourceforge.fenixedu.domain;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class ExternalTeacherAuthorization extends ExternalTeacherAuthorization_Base {

    public ExternalTeacherAuthorization() {
        super();
    }

    @Atomic
    public void revoke() {
        this.setActive(false);
        this.setRevoker(AccessControl.getPerson());
        this.setUnactiveTime(new DateTime());
    }

    public static Set<ExternalTeacherAuthorization> getExternalTeacherAuthorizationSet(ExecutionSemester executionSemester) {
        Set<ExternalTeacherAuthorization> teacherAuthorizations = new HashSet<ExternalTeacherAuthorization>();
        for (TeacherAuthorization ta : Bennu.getInstance().getTeacherAuthorizationSet()) {
            if (ta instanceof ExternalTeacherAuthorization) {
                ExternalTeacherAuthorization externalTeacherAuthorization = (ExternalTeacherAuthorization) ta;
                if (externalTeacherAuthorization.getActive()
                        && externalTeacherAuthorization.getExecutionSemester().equals(executionSemester)) {
                    teacherAuthorizations.add(externalTeacherAuthorization);
                }
            }
        }
        return teacherAuthorizations;
    }

    @Deprecated
    public boolean hasCanHaveCard() {
        return getCanHaveCard() != null;
    }

    @Deprecated
    public boolean hasActive() {
        return getActive() != null;
    }

    @Deprecated
    public boolean hasLessonHours() {
        return getLessonHours() != null;
    }

    @Deprecated
    public boolean hasAuthorizer() {
        return getAuthorizer() != null;
    }

    @Deprecated
    public boolean hasRevoker() {
        return getRevoker() != null;
    }

    @Deprecated
    public boolean hasUnactiveTime() {
        return getUnactiveTime() != null;
    }

    @Deprecated
    public boolean hasDepartment() {
        return getDepartment() != null;
    }

    @Deprecated
    public boolean hasCanPark() {
        return getCanPark() != null;
    }

}
