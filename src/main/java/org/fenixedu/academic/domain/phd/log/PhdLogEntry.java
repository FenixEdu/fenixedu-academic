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
package org.fenixedu.academic.domain.phd.log;

import java.util.Locale;

import org.fenixedu.academic.domain.phd.PhdProgramProcess;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class PhdLogEntry extends PhdLogEntry_Base {

    private PhdLogEntry() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    private PhdLogEntry(final String activityClassName, final String message, final PhdProgramProcess process) {
        setResponsible(AccessControl.getPerson());
        setWhenOccured(new DateTime());
        setActivityClassName(activityClassName);
        setPhdProgramProcess(process);
        setState(process.getActiveState().getLocalizedName(new Locale("pt", "PT")));
        setMessage(message);
    }

    public String getResponsibleName() {
        if (getResponsible() == null) {
            return "-";
        }

        return getResponsible().getName();
    }

    static PhdLogEntry createLog(final String activityClassName, final String message, final PhdProgramProcess process) {
        return new PhdLogEntry(activityClassName, message, process);
    }

}
