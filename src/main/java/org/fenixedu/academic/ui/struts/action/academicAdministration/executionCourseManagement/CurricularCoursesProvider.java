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
package org.fenixedu.academic.ui.struts.action.academicAdministration.executionCourseManagement;

import java.util.ArrayList;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.ui.renderers.providers.AbstractDomainObjectProvider;

public class CurricularCoursesProvider extends AbstractDomainObjectProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        ExecutionCourseSearchBean bean = (ExecutionCourseSearchBean) source;

        if (bean.getSemester() == null) {
            return new ArrayList<CurricularCourse>();
        }

        if (bean.getDegreeCurricularPlan() == null) {
            return new ArrayList<CurricularCourse>();
        }

        DegreeCurricularPlan degreeCurricularPlan = bean.getDegreeCurricularPlan();
        ExecutionInterval semester = bean.getSemester();

        return degreeCurricularPlan.getActiveCurricularCourses(semester);
    }

}
