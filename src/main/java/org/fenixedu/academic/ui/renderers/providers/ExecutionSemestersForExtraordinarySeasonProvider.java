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

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.ui.struts.action.student.enrollment.ExtraordinarySeasonStudentEnrollmentBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExecutionSemestersForExtraordinarySeasonProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        final ExecutionYear currentYear = ExecutionYear.readCurrentExecutionYear();
        final ExecutionYear previousYear = currentYear.getPreviousExecutionYear();
        final List<ExecutionSemester> executionSemesters = new ArrayList<ExecutionSemester>();

        ExtraordinarySeasonStudentEnrollmentBean bean = (ExtraordinarySeasonStudentEnrollmentBean) source;
        if (bean.getScp().getDegreeCurricularPlan().hasOpenExtraordinarySeasonEnrolmentPeriod(currentYear.getLastExecutionPeriod())) {
            executionSemesters.add(currentYear.getLastExecutionPeriod());
        }
        if (bean.getScp().getDegreeCurricularPlan().hasOpenExtraordinarySeasonEnrolmentPeriod(currentYear.getFirstExecutionPeriod())) {
            executionSemesters.add(currentYear.getFirstExecutionPeriod());
        }
        if (bean.getScp().getDegreeCurricularPlan().hasOpenExtraordinarySeasonEnrolmentPeriod(previousYear.getLastExecutionPeriod())) {
            executionSemesters.add(previousYear.getLastExecutionPeriod());
        }
        if (bean.getScp().getDegreeCurricularPlan().hasOpenExtraordinarySeasonEnrolmentPeriod(previousYear.getFirstExecutionPeriod())) {
            executionSemesters.add(previousYear.getFirstExecutionPeriod());
        }

        Collections.sort(executionSemesters, ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR);
        Collections.reverse(executionSemesters);
        return executionSemesters;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
