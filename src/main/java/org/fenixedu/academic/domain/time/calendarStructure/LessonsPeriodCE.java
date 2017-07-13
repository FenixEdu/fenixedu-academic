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
package org.fenixedu.academic.domain.time.calendarStructure;

import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;

public class LessonsPeriodCE extends LessonsPeriodCE_Base {

    public LessonsPeriodCE(AcademicCalendarEntry academicCalendarEntry, LocalizedString title,
            LocalizedString description, DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

        super();
        super.initEntry(academicCalendarEntry, title, description, begin, end, rootEntry);
    }

    private LessonsPeriodCE(AcademicCalendarEntry entry, LessonsPeriodCE lessonsPeriodCE) {
        super();
        super.initVirtualEntry(entry, lessonsPeriodCE);
    }

    @Override
    public boolean isOfType(AcademicPeriod period) {
        return false;
    }

    @Override
    public boolean isLessonsPerid() {
        return true;
    }

    @Override
    protected boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
        return !parentEntry.isAcademicSemester() && !parentEntry.isAcademicTrimester();
    }

    @Override
    protected boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry) {
        return true;
    }

    @Override
    protected boolean areIntersectionsPossible(AcademicCalendarEntry entryToAdd) {
        return false;
    }

    @Override
    protected boolean isPossibleToChangeTimeInterval() {
        return false;
    }

    @Override
    protected AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry) {
        return new LessonsPeriodCE(parentEntry, this);
    }

    @Override
    protected boolean associatedWithDomainEntities() {
        return false;
    }
}
