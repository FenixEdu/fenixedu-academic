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
package net.sourceforge.fenixedu.applicationTier.Servico.manager.academicCalendarManagement;

import net.sourceforge.fenixedu.dataTransferObject.manager.academicCalendarManagement.CalendarEntryBean;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarRootEntry;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicSemesterCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicTrimesterCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYearCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.EnrolmentsPeriodCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.ExamsPeriodInNormalSeasonCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.ExamsPeriodInSpecialSeasonCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.GradeSubmissionInNormalSeasonCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.GradeSubmissionInSpecialSeasonCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.LessonsPeriodCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.TeacherCreditsFillingCE;
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

            } else if (type.equals(TeacherCreditsFillingCE.class)) {
                // Do Nothing: this was created in scientific council interface.
            }

        } else {
            return bean.getEntry().edit(bean.getTitle(), bean.getDescription(), bean.getBegin(), bean.getEnd(),
                    bean.getRootEntry(), bean.getTemplateEntry());
        }

        return null;
    }
}