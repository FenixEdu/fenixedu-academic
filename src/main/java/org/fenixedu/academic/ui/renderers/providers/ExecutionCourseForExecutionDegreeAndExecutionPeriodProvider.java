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
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionDegree;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionSemester;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionCourseForExecutionDegreeAndExecutionPeriodProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();

        final HasExecutionSemester hasExecutionSemester = (HasExecutionSemester) source;
        final ExecutionSemester executionPeriod = hasExecutionSemester.getExecutionPeriod();

        final HasExecutionDegree hasExecutionDegree = (HasExecutionDegree) source;
        final ExecutionDegree executionDegree = hasExecutionDegree.getExecutionDegree();
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

        if (executionPeriod != null && executionDegree != null) {
            for (final ExecutionCourse executionCourse : executionPeriod.getAssociatedExecutionCoursesSet()) {
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
