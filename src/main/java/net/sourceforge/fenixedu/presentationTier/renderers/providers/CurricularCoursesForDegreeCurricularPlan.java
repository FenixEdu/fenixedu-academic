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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.CurricularCourseMarksheetManagementBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementBaseBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.BiDirectionalConverter;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixframework.FenixFramework;

public class CurricularCoursesForDegreeCurricularPlan implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final MarkSheetManagementBaseBean markSheetManagementBean = (MarkSheetManagementBaseBean) source;

        final List<CurricularCourseMarksheetManagementBean> result = new ArrayList<CurricularCourseMarksheetManagementBean>();

        if (markSheetManagementBean.hasDegree() && markSheetManagementBean.hasDegreeCurricularPlan()
                && markSheetManagementBean.hasExecutionPeriod()) {

            if (markSheetManagementBean.getDegree().getDegreeCurricularPlansSet()
                    .contains(markSheetManagementBean.getDegreeCurricularPlan())) {
                if (markSheetManagementBean.getDegree().isBolonhaDegree()) {
                    addCurricularCourses(
                            result,
                            markSheetManagementBean.getDegreeCurricularPlan().getDcpDegreeModules(CurricularCourse.class,
                                    markSheetManagementBean.getExecutionPeriod().getExecutionYear()),
                            markSheetManagementBean.getExecutionPeriod());
                } else {
                    addCurricularCourses(result, markSheetManagementBean.getDegreeCurricularPlan().getCurricularCourses(),
                            markSheetManagementBean.getExecutionPeriod());
                }
            } else {
                markSheetManagementBean.setDegreeCurricularPlan(null);
                markSheetManagementBean.setCurricularCourseBean(null);
            }
        }
        Collections.sort(result, CurricularCourseMarksheetManagementBean.COMPARATOR_BY_NAME);

        return result;
    }

    private void addCurricularCourses(final Collection<CurricularCourseMarksheetManagementBean> result,
            final Collection<? extends DegreeModule> dcpDegreeModules, final ExecutionSemester executionSemester) {

        for (final DegreeModule degreeModule : dcpDegreeModules) {
            result.add(new CurricularCourseMarksheetManagementBean((CurricularCourse) degreeModule, executionSemester));
        }

    }

    @Override
    public Converter getConverter() {
        return new BiDirectionalConverter() {

            @Override
            public Object convert(Class type, Object value) {
                final String str = (String) value;
                if (StringUtils.isEmpty(str)) {
                    return null;
                }
                final String[] values = str.split(":");

                final CurricularCourse course = FenixFramework.getDomainObject(values[0]);
                final ExecutionSemester semester = FenixFramework.getDomainObject(values[1]);

                return new CurricularCourseMarksheetManagementBean(course, semester);
            }

            @Override
            public String deserialize(final Object object) {
                return (object == null) ? "" : ((CurricularCourseMarksheetManagementBean) object).getKey();
            }
        };
    }
}
