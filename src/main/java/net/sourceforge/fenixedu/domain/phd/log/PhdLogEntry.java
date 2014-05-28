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
package net.sourceforge.fenixedu.domain.phd.log;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.phd.PhdProgramProcess;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

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
