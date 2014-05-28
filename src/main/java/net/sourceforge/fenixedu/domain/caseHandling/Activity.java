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
package net.sourceforge.fenixedu.domain.caseHandling;

import org.fenixedu.bennu.core.domain.User;

public abstract class Activity<P extends Process> {

    // TODO: change method return to boolean
    public abstract void checkPreConditions(P process, User userView);

    protected abstract P executeActivity(P process, User userView, Object object);

    protected void executePosConditions(P process, User userView, Object object) {
        new ProcessLog(process, userView, this);
    }

    public Boolean isVisibleForAdminOffice() {
        return Boolean.TRUE;
    }

    public Boolean isVisibleForGriOffice() {
        return Boolean.TRUE;
    }

    public Boolean isVisibleForCoordinator() {
        return Boolean.FALSE;
    }

    final public P execute(P process, User userView, Object object) {
        checkPreConditions(process, userView);
        P modifiedProcess = executeActivity(process, userView, object);
        executePosConditions(modifiedProcess, userView, object);

        log(modifiedProcess, userView, object);

        return modifiedProcess;
    }

    protected void log(P process, User userView, Object object) {

    }

    public String getId() {
        return getClass().getSimpleName();
    }

}
