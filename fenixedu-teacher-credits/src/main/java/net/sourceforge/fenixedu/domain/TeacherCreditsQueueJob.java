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
package org.fenixedu.academic.domain;

import pt.ist.fenixframework.Atomic;

public class TeacherCreditsQueueJob extends TeacherCreditsQueueJob_Base {

    public TeacherCreditsQueueJob(ExecutionSemester executionSemester) {
        super();
        setExecutionSemester(executionSemester);
    }

    @Override
    public QueueJobResult execute() throws Exception {
        TeacherCredits.closeAllTeacherCredits(getExecutionSemester());
        return new QueueJobResult();
    }

    @Atomic
    public static TeacherCreditsQueueJob createTeacherCreditsQueueJob(ExecutionSemester executionSemester) {
        return new TeacherCreditsQueueJob(executionSemester);
    }

}
