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
package net.sourceforge.fenixedu.domain.time.calendarStructure;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

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
        if (childEntry.isTeacherCreditsFilling()) {
            return getChildEntriesWithTemplateEntries(childEntry.getClass()).size() >= 1;
        }
        return false;
    }

    @Override
    protected boolean areIntersectionsPossible(AcademicCalendarEntry entryToAdd) {
        if (entryToAdd.isTeacherCreditsFilling()) {
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
    public TeacherCreditsFillingForTeacherCE getTeacherCreditsFillingForTeacher(AcademicChronology academicChronology) {
        final AcademicSemesterCE academicSemesterCE = (AcademicSemesterCE) academicChronology.findSameEntry(this);
        List<AcademicCalendarEntry> childEntries =
                academicSemesterCE.getChildEntriesWithTemplateEntries(TeacherCreditsFillingForTeacherCE.class);
        return (TeacherCreditsFillingForTeacherCE) (!childEntries.isEmpty() ? childEntries.iterator().next() : null);
    }

    @Override
    public TeacherCreditsFillingForDepartmentAdmOfficeCE getTeacherCreditsFillingForDepartmentAdmOffice(
            AcademicChronology academicChronology) {
        final AcademicSemesterCE academicSemesterCE = (AcademicSemesterCE) academicChronology.findSameEntry(this);
        List<AcademicCalendarEntry> childEntries =
                academicSemesterCE.getChildEntriesWithTemplateEntries(TeacherCreditsFillingForDepartmentAdmOfficeCE.class);
        return (TeacherCreditsFillingForDepartmentAdmOfficeCE) (!childEntries.isEmpty() ? childEntries.iterator().next() : null);
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
