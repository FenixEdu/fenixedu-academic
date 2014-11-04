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
package net.sourceforge.fenixedu.applicationTier.Servico.caseHandling;

import java.util.List;

import net.sourceforge.fenixedu.domain.caseHandling.Process;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.Pair;

public class CreateNewProcess {

    @Atomic
    public static Process run(String processName, Object object) {
        return Process.createNewProcess(Authenticate.getUser(), processName, object);
    }

    @Atomic
    public static Process run(Class<? extends Process> processClass, Object object) {
        return Process.createNewProcess(Authenticate.getUser(), processClass, object);
    }

    /**
     * *
     * 
     * <pre>
     * Args:
     * - processClass to create new
     * - object used to create new process
     * - List of Pair&lt;Class, Object&gt;
     * 		- left: activity class
     * 		- right: activity arg
     * </pre>
     * 
     * @param processClass
     * @param object
     * @param activities
     * @return
     */
    @Atomic
    public static Process run(Class<? extends Process> processClass, Object object, final List<Pair<Class<?>, Object>> activities) {
        final User userView = Authenticate.getUser();
        final Process process = Process.createNewProcess(userView, processClass, object);

        for (final Pair<Class<?>, Object> activity : activities) {
            process.executeActivity(userView, activity.getKey().getSimpleName(), activity.getValue());
        }

        return process;
    }
}