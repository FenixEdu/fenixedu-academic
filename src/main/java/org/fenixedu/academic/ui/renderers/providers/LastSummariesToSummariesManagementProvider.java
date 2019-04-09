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

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.Summary;
import org.fenixedu.academic.dto.SummariesManagementBean;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class LastSummariesToSummariesManagementProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        SummariesManagementBean bean = (SummariesManagementBean) source;
        ShiftType lessonType = bean.getLessonType();

        if (lessonType != null) {

            ExecutionCourse executionCourse = bean.getExecutionCourse();
            List<Summary> summaries = new ArrayList<Summary>();
            summaries.addAll(getSummariesByShiftType(executionCourse, lessonType));
            Collections.sort(summaries, Summary.COMPARATOR_BY_DATE_AND_HOUR);

            List<Summary> result = new ArrayList<Summary>();
            if (!summaries.isEmpty() && summaries.size() > 4) {
                result = summaries.subList(0, 4);
            } else {
                result = summaries;
            }

            if (bean.getSummary() != null) {
                result.remove(bean.getSummary());
            }

            return result;
        }
        return new ArrayList<Summary>();
    }

    private static List<Summary> getSummariesByShiftType(ExecutionCourse executionCourse, ShiftType shiftType) {
        List<Summary> summaries = new ArrayList<Summary>();
        for (Summary summary : executionCourse.getAssociatedSummariesSet()) {
            if (summary.getSummaryType() != null && summary.getSummaryType().equals(shiftType)) {
                summaries.add(summary);
            }
        }
        return summaries;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
