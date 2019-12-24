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
import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.dto.teacher.executionCourse.ImportContentBean;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionCoursesToImportLessonPlanningsProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        ImportContentBean bean = (ImportContentBean) source;
        ExecutionInterval executionSemester = bean.getExecutionPeriod();
        CurricularYear curricularYear = bean.getCurricularYear();
        DegreeCurricularPlan degreeCurricularPlan = bean.getExecutionDegree().getDegreeCurricularPlan();
        if (degreeCurricularPlan != null && executionSemester != null && curricularYear != null) {
            List<ExecutionCourse> executionCourses = getExecutionCoursesByExecutionPeriodAndSemesterAndYear(degreeCurricularPlan,
                    executionSemester, curricularYear.getYear(), executionSemester.getChildOrder());
            Collections.sort(executionCourses, ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);
            return executionCourses;
        }
        return new ArrayList<ExecutionCourse>();
    }

    private static List<ExecutionCourse> getExecutionCoursesByExecutionPeriodAndSemesterAndYear(
            final DegreeCurricularPlan degreeCurricularPlan, final ExecutionInterval interval, final Integer curricularYear,
            final Integer semester) {

        List<ExecutionCourse> result = new ArrayList<>();
        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
            for (Context context : curricularCourse.getParentContextsSet()) {
                if (context.getCurricularPeriod().getChildOrder().equals(semester)
                        && context.getCurricularYear().equals(curricularYear)) {

                    for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
                        if (executionCourse.getExecutionInterval().equals(interval)) {
                            result.add(executionCourse);
                        }
                    }
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
