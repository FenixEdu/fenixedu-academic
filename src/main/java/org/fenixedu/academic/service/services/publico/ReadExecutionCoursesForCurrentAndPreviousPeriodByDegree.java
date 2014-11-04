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
package org.fenixedu.academic.service.services.publico;

import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.dto.ExecutionCourseView;
import org.fenixedu.academic.util.PeriodState;

import pt.ist.fenixframework.Atomic;

public class ReadExecutionCoursesForCurrentAndPreviousPeriodByDegree {

    @Atomic
    public static Set<ExecutionCourseView> run(final Degree degree) {
        final ExecutionSemester currentExecutionPeriod = ExecutionSemester.readActualExecutionSemester();
        final ExecutionSemester nextExecutionSemester = currentExecutionPeriod.getNextExecutionPeriod();
        final ExecutionSemester previousExecutionPeriod;
        if (nextExecutionSemester != null && nextExecutionSemester.getState().equals(PeriodState.OPEN)) {
            previousExecutionPeriod = nextExecutionSemester;
        } else {
            previousExecutionPeriod = currentExecutionPeriod.getPreviousExecutionPeriod();
        }

        final Set<ExecutionCourseView> result = new HashSet<ExecutionCourseView>();
        for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
            if (degreeCurricularPlan.isActive()) {
                degreeCurricularPlan.addExecutionCourses(result, currentExecutionPeriod, previousExecutionPeriod);
            }
        }
        return result;
    }

}