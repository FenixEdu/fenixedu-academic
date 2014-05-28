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
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcess;

import org.fenixedu.bennu.core.domain.User;

public class PhdLog {

    public static PhdLogEntry logActivity(final Activity activity, final PhdProgramProcess process, final User userView,
            final Object object) {

        ResourceBundle bundle = ResourceBundle.getBundle("resources/PhdResources", new Locale("pt", "PT"));

        String className = activity.getClass().getName();
        String message = bundle.getString("label." + className);

        return PhdLogEntry.createLog(className, message, process);
    }

}
