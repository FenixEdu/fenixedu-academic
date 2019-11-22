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
package org.fenixedu.academic.domain.time.chronologies;

import org.fenixedu.academic.domain.time.calendarStructure.AcademicCalendarEntry;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicCalendarRootEntry;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicSemesterCE;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicYearCE;
import org.fenixedu.academic.domain.time.chronologies.dateTimeFields.AcademicSemesterDateTimeField;
import org.fenixedu.academic.domain.time.chronologies.dateTimeFields.AcademicSemesterOfAcademicYearDateTimeField;
import org.fenixedu.academic.domain.time.chronologies.dateTimeFields.AcademicYearDateTimeField;
import org.fenixedu.academic.domain.time.chronologies.dateTimeFields.DayOfAcademicSemesterDateTimeField;
import org.fenixedu.academic.domain.time.chronologies.durationFields.AcademicSemestersDurationField;
import org.fenixedu.academic.domain.time.chronologies.durationFields.AcademicYearsDurationField;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.Interval;
import org.joda.time.chrono.AssembledChronology;
import org.joda.time.chrono.ISOChronology;

public class AcademicChronology extends AssembledChronology {

    private transient AcademicCalendarRootEntry academicCalendar;

    // Duration Fields
    private transient DurationField acAcademicYearsField;

    private transient DurationField acAcademicSemestersField;

    private transient DurationField acAcademicTrimestersField;

    // DateTime Fields
    private transient DateTimeField acAcademicYear;

    private transient DateTimeField acAcademicSemester;

    private transient DateTimeField acAcademicSemesterOfAcademicYear;

    private transient DateTimeField acDayOfAcademicSemester;

    private transient DateTimeField acAcademicTrimester;

    // Static Variables
    private static final ISOChronology ISO_INSTANCE = ISOChronology.getInstance();

    // Override Methods

    public AcademicChronology(AcademicCalendarRootEntry academicCalendar) {
        super(ISO_INSTANCE, null);
        this.academicCalendar = academicCalendar;
    }

    @Override
    protected void assemble(Fields fields) {
        if (acAcademicYearsField == null) {
            acAcademicYearsField = new AcademicYearsDurationField(this);
            acAcademicYear = new AcademicYearDateTimeField(this);

            acAcademicSemester = new AcademicSemesterDateTimeField(this);
            acAcademicSemestersField = new AcademicSemestersDurationField(this);
            acAcademicSemesterOfAcademicYear = new AcademicSemesterOfAcademicYearDateTimeField(this);
            acDayOfAcademicSemester = new DayOfAcademicSemesterDateTimeField(this);
        }
    }

    @Override
    public Chronology withUTC() {
        return ISOChronology.getInstanceUTC();
    }

    @Override
    public Chronology withZone(DateTimeZone zone) {
        return ISO_INSTANCE.withZone(zone);
    }

    @Override
    public String toString() {
        String str = "AcademicChronology";
        DateTimeZone zone = getZone();
        if (zone != null) {
            str = str + '[' + zone.getID() + ']';
        }
        return str;
    }

    // DateTime Fields

    public DateTimeField academicYear() {
        return acAcademicYear;
    }

    public DateTimeField academicSemester() {
        return acAcademicSemester;
    }

    public DateTimeField academicTrimester() {
        return acAcademicTrimester;
    }

    public DateTimeField academicSemesterOfAcademicYear() {
        return acAcademicSemesterOfAcademicYear;
    }

    public DateTimeField dayOfAcademicSemester() {
        return acDayOfAcademicSemester;
    }

    // Duration Fields

    public DurationField academicYears() {
        return acAcademicYearsField;
    }

    public DurationField academicSemesters() {
        return acAcademicSemestersField;
    }

    public DurationField academicTrimesters() {
        return acAcademicTrimestersField;
    }

    // Auxiliar Methods

    public AcademicSemesterCE getAcademicSemesterIn(int index) {
        return (AcademicSemesterCE) academicCalendar.getEntryByIndex(index, AcademicSemesterCE.class, AcademicYearCE.class);
    }

    public AcademicYearCE getAcademicYearIn(int index) {
        return (AcademicYearCE) academicCalendar.getEntryByIndex(index, AcademicYearCE.class, AcademicCalendarRootEntry.class);
    }

    public int getAcademicSemester(long instant) {
        Integer entryValueByInstant =
                academicCalendar.getEntryIndexByInstant(instant, AcademicSemesterCE.class, AcademicYearCE.class);
        if (entryValueByInstant != null) {
            return entryValueByInstant;
        }
        return 0;
    }

    public int getAcademicYear(long instant) {
        Integer entryValueByInstant =
                academicCalendar.getEntryIndexByInstant(instant, AcademicYearCE.class, AcademicCalendarRootEntry.class);
        if (entryValueByInstant != null) {
            return entryValueByInstant;
        }
        return 0;
    }

    public int getDayOfAcademicSemester(long instant) {
        AcademicCalendarEntry entryByInstant =
                academicCalendar.getEntryByInstant(instant, AcademicSemesterCE.class, AcademicYearCE.class);
        if (entryByInstant != null) {
            DateTime instantDateTime = new DateTime(instant);
            Interval interval = new Interval(entryByInstant.getBegin(), instantDateTime);
            int days = interval.toPeriod().getDays();
            if (days > 0) {
                return days;
            }
        }
        return 0;
    }

    public int getAcademicSemesterOfAcademicYear(long instant) {
        AcademicSemesterCE entryByInstant =
                (AcademicSemesterCE) academicCalendar.getEntryByInstant(instant, AcademicSemesterCE.class, AcademicYearCE.class);
//        return entryByInstant.getAcademicSemesterOfAcademicYear(this);
        return entryByInstant.getCardinality();
    }

    public int getMaximumValueForAcademicSemesterOfAcademicYear(long instant) {
        int academicSemesterOfAcademicYear = getAcademicSemesterOfAcademicYear(instant);
        if (academicSemesterOfAcademicYear == 0) {
            return getMaximumValueForAcademicSemesterOfAcademicYear();
        }
        return getMaximumValueForAcademicSemesterOfAcademicYear() - academicSemesterOfAcademicYear;
    }

    public int getMaximumValueForAcademicSemesterOfAcademicYear() {
        return 2;
    }

    public AcademicCalendarEntry findSameEntry(final AcademicCalendarEntry entry) {
        return entry.getEntryForCalendar(academicCalendar);
    }
}
