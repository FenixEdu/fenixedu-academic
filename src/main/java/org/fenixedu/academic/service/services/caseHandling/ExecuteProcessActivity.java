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
package org.fenixedu.academic.service.services.caseHandling;

import java.util.List;

import org.fenixedu.academic.domain.caseHandling.Process;
import org.fenixedu.academic.util.Pair;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.Atomic;

public class ExecuteProcessActivity {

    @Atomic
    static public Process run(final Process process, final String activityId, final Object object) {
        return process.executeActivity(Authenticate.getUser(), activityId, object);
    }

    @Atomic
    static public Process run(final Process process, final Class<?> clazz, final Object object) {
        return run(Authenticate.getUser(), process, clazz, object);
    }

    @Atomic
    static private Process run(final User userView, final Process process, final Class<?> clazz, final Object object) {
        return process.executeActivity(userView, clazz.getSimpleName(), object);
    }

    /**
     * <pre>
     * Pair&lt;String, Object&gt;
     * - left: activity id
     * - right: activity arg
     * </pre>
     * 
     * @param process
     * @param activities
     * @return
     */
    @Atomic
    static public Process run(final Process process, final List<Pair<String, Object>> activities) {
        final User userView = Authenticate.getUser();
        for (final Pair<String, Object> activity : activities) {
            process.executeActivity(userView, activity.getKey(), activity.getValue());
        }

        return process;
    }

}