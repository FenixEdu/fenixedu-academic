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

import java.util.List;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.time.chronologies.AcademicChronology;
import org.fenixedu.academic.util.MultiLanguageString;
import org.joda.time.DateTime;

public class AcademicSemesterCE extends AcademicSemesterCE_Base {

    public AcademicSemesterCE(AcademicCalendarEntry parentEntry, MultiLanguageString title, MultiLanguageString description,
            DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

        super();
        super.initEntry(parentEntry, title, description, begin, end, rootEntry);
        createNewExecutionPeriod();
    }

    private AcademicSemesterCE(AcademicCalendarEntry parentEntry, AcademicSemesterCE academicSemesterCE) {
        super();
        super.initVirtualEntry(parentEntry, academicSemesterCE);
    }

    @Override
    public void delete(AcademicCalendarRootEntry rootEntry) {
        if (!isVirtual()) {
            ExecutionSemester executionSemester = ExecutionSemester.getExecutionPeriod(this);
            executionSemester.delete();
        }
        super.delete(rootEntry);
    }

    @Override
    protected void beforeRedefineEntry() {
        throw new DomainException("error.unsupported.operation");
    }

    @Override
    protected void afterRedefineEntry() {
        throw new DomainException("error.unsupported.operation");
    }

    @Override
    public boolean isOfType(AcademicPeriod period) {
        return period.equals(AcademicPeriod.SEMESTER);
    }

    @Override
    public boolean isAcademicSemester() {
        return true;
    }

    @Override
    protected boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
        return !parentEntry.isAcademicYear();
    }

    @Override
    protected boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry) {
        if (childEntry.isAcademicTrimester()) {
            return getChildEntriesWithTemplateEntries(childEntry.getClass()).size() >= 2;
        }
        return false;
    }

    @Override
    protected boolean areIntersectionsPossible(AcademicCalendarEntry entryToAdd) {
        return false;
    }

    @Override
    protected boolean isPossibleToChangeTimeInterval() {
        return true;
    }

    @Override
    protected AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry) {
        return new AcademicSemesterCE(parentEntry, this);
    }

    private void createNewExecutionPeriod() {
        ExecutionYear executionYear = ExecutionYear.getExecutionYear((AcademicYearCE) getParentEntry());
        new ExecutionSemester(executionYear, new AcademicInterval(this, getRootEntry()), getTitle().getContent());
    }

    @Override
    public int getAcademicSemesterOfAcademicYear(final AcademicChronology academicChronology) {
        final AcademicYearCE academicYearCE = (AcademicYearCE) academicChronology.findSameEntry(getParentEntry());
        List<AcademicCalendarEntry> list =
                academicYearCE.getChildEntriesWithTemplateEntries(academicYearCE.getBegin(), getBegin().minusDays(1), getClass());
        return list.size() + 1;
    }

    @Override
    protected boolean associatedWithDomainEntities() {
        return true;
    }

    @Override
    public String getPresentationName() {
        return getParentEntry().getTitle().getContent() + " - " + getTitle().getContent() + " - [" + getType() + "]";
    }
}
