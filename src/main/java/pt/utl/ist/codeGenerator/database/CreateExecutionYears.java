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
package pt.utl.ist.codeGenerator.database;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.oldInquiries.InquiryResponsePeriod;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarRootEntry;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicSemesterCE;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYearCE;
import net.sourceforge.fenixedu.util.PeriodState;

import org.fenixedu.commons.i18n.I18N;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateExecutionYears {
    public static void createExecutionYears() {
        I18N.setLocale(Locale.getDefault());

        final int numYearsToCreate = 5;
        final YearMonthDay today = new YearMonthDay();
        final YearMonthDay yearMonthDay = new YearMonthDay(today.getYear() - numYearsToCreate + 2, 9, 1);
        AcademicCalendarRootEntry rootEntry =
                new AcademicCalendarRootEntry(new MultiLanguageString("Calendario Academico"), null, null);
        for (int i = 0; i < numYearsToCreate; createExecutionYear(yearMonthDay, i++, rootEntry)) {
            ;
        }
    }

    private static void createExecutionYear(final YearMonthDay yearMonthDay, final int offset, AcademicCalendarRootEntry rootEntry) {

        final int year = yearMonthDay.getYear() + offset;
        final YearMonthDay start = new YearMonthDay(year, yearMonthDay.getMonthOfYear(), yearMonthDay.getDayOfMonth());
        final YearMonthDay end = new YearMonthDay(year + 1, 8, 31);

        AcademicYearCE academicYear =
                new AcademicYearCE(rootEntry, new MultiLanguageString(getYearString(year)), null, start.toDateTimeAtMidnight(),
                        end.toDateTimeAtMidnight(), rootEntry);

        ExecutionYear executionYear = ExecutionYear.getExecutionYear(academicYear);

        final YearMonthDay now = new YearMonthDay();
        if (start.isAfter(now) || end.isBefore(now)) {
            executionYear.setState(PeriodState.OPEN);
        } else {
            executionYear.setState(PeriodState.CURRENT);
        }

        createExecutionPeriods(executionYear, academicYear);
    }

    private static void createExecutionPeriods(final ExecutionYear executionYear, AcademicYearCE academicYear) {
        createExecutionPeriods(executionYear, 1, academicYear);
        createExecutionPeriods(executionYear, 2, academicYear);
    }

    private static void createExecutionPeriods(final ExecutionYear executionYear, final int semester, AcademicYearCE academicYear) {

        final YearMonthDay start = getStartYearMonthDay(executionYear, semester);
        final YearMonthDay end = getEndYearMonthDay(executionYear, semester);

        AcademicSemesterCE academicSemester =
                new AcademicSemesterCE(academicYear, new MultiLanguageString(getPeriodString(semester)), null,
                        start.toDateTimeAtMidnight(), end.toDateTimeAtMidnight(), academicYear.getRootEntry());

        ExecutionSemester executionPeriod = ExecutionSemester.getExecutionPeriod(academicSemester);

        final YearMonthDay now = new YearMonthDay();
        if (start.isAfter(now) || end.isBefore(now)) {
            executionPeriod.setState(PeriodState.OPEN);
        } else {
            executionPeriod.setState(PeriodState.CURRENT);
        }

        createInquiryResponsePeriods(executionPeriod);
    }

    private static void createInquiryResponsePeriods(final ExecutionSemester executionPeriod) {
        new InquiryResponsePeriod(executionPeriod, executionPeriod.getBeginDate(), executionPeriod.getEndDate());
    }

    private static YearMonthDay getStartYearMonthDay(final ExecutionYear executionYear, final int semester) {
        final YearMonthDay yearMonthDay = executionYear.getBeginDateYearMonthDay();
        return semester == 1 ? yearMonthDay : new YearMonthDay(yearMonthDay.getYear() + 1, 2, 1);
    }

    private static YearMonthDay getEndYearMonthDay(final ExecutionYear executionYear, final int semester) {
        final YearMonthDay yearMonthDay = executionYear.getEndDateYearMonthDay();
        return semester == 2 ? yearMonthDay : new YearMonthDay(yearMonthDay.getYear(), 1, 31);
    }

    private static String getPeriodString(final int semester) {
        return "Semester " + semester;
    }

    private static String getYearString(final int year) {
        return Integer.toString(year) + '/' + (year + 1);
    }
}
