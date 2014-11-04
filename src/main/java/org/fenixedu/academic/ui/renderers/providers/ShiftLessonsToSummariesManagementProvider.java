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

import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.dto.SummariesManagementBean;
import org.fenixedu.academic.dto.SummariesManagementBean.SummaryType;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ShiftLessonsToSummariesManagementProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        SummariesManagementBean bean = (SummariesManagementBean) source;
        Shift shift = bean.getShift();
        SummaryType summaryType = bean.getSummaryType();
        Set<Lesson> lessons = new TreeSet<Lesson>(Lesson.LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME);
        if (shift != null && summaryType != null && summaryType.equals(SummaryType.NORMAL_SUMMARY)) {
            lessons.addAll(shift.getAssociatedLessonsSet());
        }
        return lessons;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
