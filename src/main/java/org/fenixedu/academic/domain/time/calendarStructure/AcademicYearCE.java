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

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.time.chronologies.AcademicChronology;
import org.fenixedu.academic.util.MultiLanguageString;
import org.joda.time.DateTime;

public class AcademicYearCE extends AcademicYearCE_Base {

    public AcademicYearCE(AcademicCalendarEntry parentEntry, MultiLanguageString title, MultiLanguageString description,
            DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

        super();
        super.initEntry(parentEntry, title, description, begin, end, rootEntry);
        createExecutionYear();
    }

    private AcademicYearCE(AcademicCalendarEntry parentEntry, AcademicYearCE academicYearCE) {
        super();
        super.initVirtualEntry(parentEntry, academicYearCE);
    }

    @Override
    public void delete(AcademicCalendarRootEntry rootEntry) {
        if (!isVirtual()) {
            ExecutionYear executionYear = ExecutionYear.getExecutionYear(this);
            executionYear.delete();
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
        return period.equals(AcademicPeriod.YEAR);
    }

    @Override
    public boolean isAcademicYear() {
        return true;
    }

    @Override
    protected boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
        return !parentEntry.isRoot();
    }

    @Override
    protected boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry) {
        if (childEntry.isAcademicSemester()) {
            return getChildEntriesWithTemplateEntries(childEntry.getClass()).size() >= 2;
        }
        if (childEntry.isAcademicTrimester()) {
            return getChildEntriesWithTemplateEntries(childEntry.getClass()).size() >= 4;
        }
        return false;
    }

    @Override
    protected boolean areIntersectionsPossible(AcademicCalendarEntry entryToAdd) {
        if (entryToAdd.isAcademicSemester()) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean isPossibleToChangeTimeInterval() {
        return true;
    }

    @Override
    protected AcademicCalendarEntry createVirtualEntry(AcademicCalendarEntry parentEntry) {
        return new AcademicYearCE(parentEntry, this);
    }

    private void createExecutionYear() {
        ExecutionYear executionYear = ExecutionYear.readBy(getBegin().toYearMonthDay(), getEnd().toYearMonthDay());
        if (executionYear == null) {
            new ExecutionYear(new AcademicInterval(this, getRootEntry()), getTitle().getContent());
        }
    }

    @Override
    public int getAcademicSemesterOfAcademicYear(AcademicChronology academicChronology) {
        throw new DomainException("error.unsupported.operation");
    }

    @Override
    protected boolean associatedWithDomainEntities() {
        return true;
    }
}
