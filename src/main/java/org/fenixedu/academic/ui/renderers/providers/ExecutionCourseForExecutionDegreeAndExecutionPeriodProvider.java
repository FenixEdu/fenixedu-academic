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
package org.fenixedu.academic.ui.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.interfaces.HasExecutionDegree;
import org.fenixedu.academic.domain.interfaces.HasExecutionSemester;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionCourseForExecutionDegreeAndExecutionPeriodProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();

        final HasExecutionSemester hasExecutionSemester = (HasExecutionSemester) source;
        final ExecutionInterval executionInterval = hasExecutionSemester.getExecutionPeriod();

        final HasExecutionDegree hasExecutionDegree = (HasExecutionDegree) source;
        final ExecutionDegree executionDegree = hasExecutionDegree.getExecutionDegree();
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

        if (executionInterval != null && executionDegree != null) {
            for (final ExecutionCourse executionCourse : executionInterval.getAssociatedExecutionCoursesSet()) {
                if (matches(executionCourse, degreeCurricularPlan)) {
                    executionCourses.add(executionCourse);
                }
            }
        }

        Collections.sort(executionCourses, ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);

        return executionCourses;
    }

    private boolean matches(final ExecutionCourse executionCourse, final DegreeCurricularPlan degreeCurricularPlan) {
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            if (curricularCourse.getDegreeCurricularPlan() == degreeCurricularPlan) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
