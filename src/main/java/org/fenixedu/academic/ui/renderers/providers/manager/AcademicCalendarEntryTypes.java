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
package org.fenixedu.academic.ui.renderers.providers.manager;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.time.calendarStructure.AcademicCalendarEntry;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicIntervalCE;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicYearCE;
import org.fenixedu.academic.dto.manager.academicCalendarManagement.CalendarEntryBean;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AcademicCalendarEntryTypes implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        List<Class<? extends AcademicCalendarEntry>> result = new ArrayList<Class<? extends AcademicCalendarEntry>>();
        CalendarEntryBean bean = (CalendarEntryBean) source;

        if (bean.getEntry() != null && bean.getEntry().isRoot()) {

            result.add(AcademicYearCE.class);
            result.add(AcademicIntervalCE.class);
//            result.add(AcademicTrimesterCE.class);

        } else if (bean.getEntry() != null) {

            result.add(AcademicIntervalCE.class);
//            result.add(AcademicTrimesterCE.class);
//            result.add(LessonsPeriodCE.class);
//            result.add(EnrolmentsPeriodCE.class);
//            result.add(ExamsPeriodInNormalSeasonCE.class);
//            result.add(ExamsPeriodInSpecialSeasonCE.class);
//            result.add(GradeSubmissionInNormalSeasonCE.class);
//            result.add(GradeSubmissionInSpecialSeasonCE.class);
        }

        return result;
    }

    @Override
    public Converter getConverter() {
        return null;
    }
}
