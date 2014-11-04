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

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.student.enrollment.SpecialSeasonStudentEnrollmentBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionSemestersForSpecialSeasonProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        final ExecutionYear currentYear = ExecutionYear.readCurrentExecutionYear();
        final ExecutionYear previousYear = currentYear.getPreviousExecutionYear();
        final List<ExecutionSemester> executionSemesters = new ArrayList<ExecutionSemester>();

        SpecialSeasonStudentEnrollmentBean bean = (SpecialSeasonStudentEnrollmentBean) source;
        if (bean.getScp().getDegreeCurricularPlan().hasOpenSpecialSeasonEnrolmentPeriod(currentYear.getLastExecutionPeriod())) {
            executionSemesters.add(currentYear.getLastExecutionPeriod());
        }
        if (bean.getScp().getDegreeCurricularPlan().hasOpenSpecialSeasonEnrolmentPeriod(currentYear.getFirstExecutionPeriod())) {
            executionSemesters.add(currentYear.getFirstExecutionPeriod());
        }
        if (bean.getScp().getDegreeCurricularPlan().hasOpenSpecialSeasonEnrolmentPeriod(previousYear.getLastExecutionPeriod())) {
            executionSemesters.add(previousYear.getLastExecutionPeriod());
        }
        if (bean.getScp().getDegreeCurricularPlan().hasOpenSpecialSeasonEnrolmentPeriod(previousYear.getFirstExecutionPeriod())) {
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
