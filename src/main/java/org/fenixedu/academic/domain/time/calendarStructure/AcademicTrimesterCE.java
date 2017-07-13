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

public class AcademicTrimesterCE extends AcademicTrimesterCE_Base {

    public AcademicTrimesterCE(AcademicCalendarEntry parentEntry, LocalizedString title, LocalizedString description,
            DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

        super();
        super.initEntry(parentEntry, title, description, begin, end, rootEntry);
    }

    private AcademicTrimesterCE(AcademicCalendarEntry parentEntry, AcademicTrimesterCE academicTrimesterCE) {
        super();
        super.initVirtualEntry(parentEntry, academicTrimesterCE);
    }

    @Override
    public boolean isOfType(AcademicPeriod period) {
        return period.equals(AcademicPeriod.TRIMESTER);
    }

    @Override
    public boolean isAcademicTrimester() {
        return true;
    }

    @Override
    protected boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
        return !parentEntry.isAcademicYear() && !parentEntry.isAcademicSemester();
    }

    @Override
    protected boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry) {
        return false;
    }

    @Override
    protected boolean areIntersectionsPossible(AcademicCalendarEntry entryToAdd) {
        return true;
    }

    @Override
    protected boolean isPossibleToChangeTimeInterval() {
        return true;
    }

    @Override
    protected AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry) {
        return new AcademicTrimesterCE(parentEntry, this);
    }

    @Override
    protected boolean associatedWithDomainEntities() {
        return false;
    }
}
