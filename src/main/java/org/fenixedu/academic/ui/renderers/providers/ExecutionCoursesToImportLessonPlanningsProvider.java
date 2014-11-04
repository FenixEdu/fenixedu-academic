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

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.ImportContentBean;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionCoursesToImportLessonPlanningsProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        ImportContentBean bean = (ImportContentBean) source;
        ExecutionSemester executionSemester = bean.getExecutionPeriod();
        CurricularYear curricularYear = bean.getCurricularYear();
        DegreeCurricularPlan degreeCurricularPlan = bean.getExecutionDegree().getDegreeCurricularPlan();
        if (degreeCurricularPlan != null && executionSemester != null && curricularYear != null) {
            List<ExecutionCourse> executionCourses =
                    degreeCurricularPlan.getExecutionCoursesByExecutionPeriodAndSemesterAndYear(executionSemester,
                            curricularYear.getYear(), executionSemester.getSemester());
            Collections.sort(executionCourses, ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);
            return executionCourses;
        }
        return new ArrayList<ExecutionCourse>();
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
