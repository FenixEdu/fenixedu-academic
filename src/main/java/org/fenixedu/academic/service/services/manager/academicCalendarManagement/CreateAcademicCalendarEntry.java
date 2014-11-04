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
package org.fenixedu.academic.service.services.manager.academicCalendarManagement;

import org.fenixedu.academic.domain.time.calendarStructure.AcademicCalendarEntry;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicCalendarRootEntry;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicSemesterCE;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicTrimesterCE;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicYearCE;
import org.fenixedu.academic.domain.time.calendarStructure.EnrolmentsPeriodCE;
import org.fenixedu.academic.domain.time.calendarStructure.ExamsPeriodInNormalSeasonCE;
import org.fenixedu.academic.domain.time.calendarStructure.ExamsPeriodInSpecialSeasonCE;
import org.fenixedu.academic.domain.time.calendarStructure.GradeSubmissionInNormalSeasonCE;
import org.fenixedu.academic.domain.time.calendarStructure.GradeSubmissionInSpecialSeasonCE;
import org.fenixedu.academic.domain.time.calendarStructure.LessonsPeriodCE;
import org.fenixedu.academic.dto.manager.academicCalendarManagement.CalendarEntryBean;

import pt.ist.fenixframework.Atomic;

public class CreateAcademicCalendarEntry {

    @Atomic
    public static AcademicCalendarEntry run(CalendarEntryBean bean, boolean toCreate) {

        if (toCreate) {

            Class<? extends AcademicCalendarEntry> type = bean.getType();

            if (type.equals(AcademicCalendarRootEntry.class)) {
                return new AcademicCalendarRootEntry(bean.getTitle(), bean.getDescription(), bean.getTemplateEntry());

            } else if (type.equals(AcademicYearCE.class)) {
                return new AcademicYearCE(bean.getEntry(), bean.getTitle(), bean.getDescription(), bean.getBegin(),
                        bean.getEnd(), bean.getRootEntry());

            } else if (type.equals(AcademicSemesterCE.class)) {
                return new AcademicSemesterCE(bean.getEntry(), bean.getTitle(), bean.getDescription(), bean.getBegin(),
                        bean.getEnd(), bean.getRootEntry());

            } else if (type.equals(AcademicTrimesterCE.class)) {
                return new AcademicTrimesterCE(bean.getEntry(), bean.getTitle(), bean.getDescription(), bean.getBegin(),
                        bean.getEnd(), bean.getRootEntry());

            } else if (type.equals(LessonsPeriodCE.class)) {
                return new LessonsPeriodCE(bean.getEntry(), bean.getTitle(), bean.getDescription(), bean.getBegin(),
                        bean.getEnd(), bean.getRootEntry());

            } else if (type.equals(ExamsPeriodInNormalSeasonCE.class)) {
                return new ExamsPeriodInNormalSeasonCE(bean.getEntry(), bean.getTitle(), bean.getDescription(), bean.getBegin(),
                        bean.getEnd(), bean.getRootEntry());

            } else if (type.equals(ExamsPeriodInSpecialSeasonCE.class)) {
                return new ExamsPeriodInSpecialSeasonCE(bean.getEntry(), bean.getTitle(), bean.getDescription(), bean.getBegin(),
                        bean.getEnd(), bean.getRootEntry());

            } else if (type.equals(GradeSubmissionInNormalSeasonCE.class)) {
                return new GradeSubmissionInNormalSeasonCE(bean.getEntry(), bean.getTitle(), bean.getDescription(),
                        bean.getBegin(), bean.getEnd(), bean.getRootEntry());

            } else if (type.equals(GradeSubmissionInSpecialSeasonCE.class)) {
                return new GradeSubmissionInSpecialSeasonCE(bean.getEntry(), bean.getTitle(), bean.getDescription(),
                        bean.getBegin(), bean.getEnd(), bean.getRootEntry());

            } else if (type.equals(EnrolmentsPeriodCE.class)) {
                return new EnrolmentsPeriodCE(bean.getEntry(), bean.getTitle(), bean.getDescription(), bean.getBegin(),
                        bean.getEnd(), bean.getRootEntry());
            }

        } else {
            return bean.getEntry().edit(bean.getTitle(), bean.getDescription(), bean.getBegin(), bean.getEnd(),
                    bean.getRootEntry(), bean.getTemplateEntry());
        }

        return null;
    }
}