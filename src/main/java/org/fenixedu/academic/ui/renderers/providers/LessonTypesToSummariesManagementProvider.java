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

import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.Summary;
import org.fenixedu.academic.dto.SummariesManagementBean;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;

public class LessonTypesToSummariesManagementProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        SummariesManagementBean bean = (SummariesManagementBean) source;
        Lesson lesson = bean.getLesson();
        Summary summary = bean.getSummary();
        Set<ShiftType> shiftTypes = new HashSet<ShiftType>();

        if (summary != null && summary.getSummaryType() != null) {
            shiftTypes.add(summary.getSummaryType());
        }

        if (lesson != null) {
            shiftTypes.addAll(lesson.getShift().getTypes());
        }

        return shiftTypes;
    }

    @Override
    public Converter getConverter() {
        return new EnumConverter();
    }
}
