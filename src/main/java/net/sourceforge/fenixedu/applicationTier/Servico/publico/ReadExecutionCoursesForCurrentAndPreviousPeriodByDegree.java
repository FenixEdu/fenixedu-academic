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
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseView;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.util.PeriodState;
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
        for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlans()) {
            if (degreeCurricularPlan.isActive()) {
                degreeCurricularPlan.addExecutionCourses(result, currentExecutionPeriod, previousExecutionPeriod);
            }
        }
        return result;
    }

}