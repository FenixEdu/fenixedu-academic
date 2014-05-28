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

import net.sourceforge.fenixedu.domain.credits.CreditsState;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class TeacherCreditsState extends TeacherCreditsState_Base {

    public TeacherCreditsState(ExecutionSemester executionSemester) {
        super();
        setExecutionSemester(executionSemester);
        setBasicOperations();
        setCloseState();
        setRootDomainObject(Bennu.getInstance());
    }

    public boolean isOpenState() {
        return getCreditState() == CreditsState.OPEN;
    }

    public boolean isCloseState() {
        return getCreditState() == CreditsState.CLOSE;
    }

    public void setOpenState() {
        setCreditState(CreditsState.OPEN);
        setBasicOperations();
    }

    public void setCloseState() {
        setCreditState(CreditsState.CLOSE);
        setBasicOperations();
    }

    private void setBasicOperations() {
        setPerson(AccessControl.getPerson());
        setLastModifiedDate(new DateTime());
    }

    public static TeacherCreditsState getTeacherCreditsState(ExecutionSemester executionSemester) {
        for (TeacherCreditsState teacherCreditsState : Bennu.getInstance().getTeacherCreditsStateSet()) {
            if (teacherCreditsState.getExecutionSemester().equals(executionSemester)) {
                return teacherCreditsState;
            }
        }
        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.TeacherCredits> getTeacherCredits() {
        return getTeacherCreditsSet();
    }

    @Deprecated
    public boolean hasAnyTeacherCredits() {
        return !getTeacherCreditsSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasLastModifiedDate() {
        return getLastModifiedDate() != null;
    }

    @Deprecated
    public boolean hasExecutionSemester() {
        return getExecutionSemester() != null;
    }

    @Deprecated
    public boolean hasCreditState() {
        return getCreditState() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
