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
import java.util.List;

import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.Summary;
import org.fenixedu.academic.dto.SummariesManagementBean;
import org.fenixedu.academic.dto.SummariesManagementBean.SummaryType;
import org.fenixedu.academic.ui.renderers.converters.YearMonthDayConverter;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PossibleDatesToSummariesManagementProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        SummariesManagementBean bean = (SummariesManagementBean) source;
        Lesson lesson = bean.getLesson();
        Shift shift = bean.getShift();
        SummaryType summaryType = bean.getSummaryType();
        Summary summary = bean.getSummary();
        List<YearMonthDay> possibleSummaryDates = new ArrayList<YearMonthDay>();

        if (summaryType != null && summaryType.equals(SummaryType.NORMAL_SUMMARY)) {
            if (lesson != null) {
                possibleSummaryDates.addAll(lesson.getAllPossibleDatesToInsertSummary());
            }

            // Show SummaryDate when edit summary
            if (summary != null) {
                Shift summaryShift = summary.getShift();
                Lesson summaryLesson = summary.getLesson();
                if (shift != null && lesson != null && summaryShift != null && summaryShift.equals(shift)
                        && summaryLesson != null && summaryLesson.equals(lesson)) {
                    possibleSummaryDates.add(0, summary.getSummaryDateYearMonthDay());
                }
            }
        }
        return possibleSummaryDates;
    }

    @Override
    public Converter getConverter() {
        return new YearMonthDayConverter();
    }
}
