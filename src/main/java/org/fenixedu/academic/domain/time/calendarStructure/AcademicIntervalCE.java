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

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.util.PeriodState;
import org.fenixedu.commons.i18n.LocalizedString;
import org.joda.time.DateTime;

public class AcademicIntervalCE extends AcademicIntervalCE_Base {

    public AcademicIntervalCE(AcademicPeriod academicPeriod, AcademicCalendarEntry parentEntry, LocalizedString title,
            LocalizedString description, DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

        super();
        setAcademicPeriod(academicPeriod);
        super.initEntry(parentEntry, title, description, begin, end, rootEntry);
        createNewExecutionPeriod();
    }

    @Override
    public void delete(AcademicCalendarRootEntry rootEntry) {
        ExecutionInterval executionInterval = ExecutionInterval.getExecutionInterval(this);
        if (executionInterval != null) {
            executionInterval.delete();
        }
        super.delete(rootEntry);
    }

    @Override
    public AcademicPeriod getAcademicPeriod() {
        return super.getAcademicPeriod() != null ? super.getAcademicPeriod() : AcademicPeriod.SEMESTER;
    }

    @Override
    protected boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
        return !parentEntry.isAcademicYear();
    }

    @Override
    protected boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry) {
        return true;
    }

    private void createNewExecutionPeriod() {
        ExecutionYear executionYear = ExecutionYear.getExecutionYear((AcademicYearCE) getParentEntry());
        new ExecutionInterval(executionYear, new AcademicInterval(this, getRootEntry()), getTitle().getContent())
                .setState(PeriodState.OPEN);
    }

//    @Override
//    public int getAcademicSemesterOfAcademicYear(final AcademicChronology academicChronology) {
//        final AcademicYearCE academicYearCE = (AcademicYearCE) academicChronology.findSameEntry(getParentEntry());
//        List<AcademicCalendarEntry> list =
//                academicYearCE.getChildEntries(academicYearCE.getBegin(), getBegin().minusDays(1), getClass());
//        return list.size() + 1;
//    }

    @Override
    public String getPresentationName() {
        return getParentEntry().getTitle().getContent() + " - " + getTitle().getContent() + " - [" + getType() + "]";
    }
}
