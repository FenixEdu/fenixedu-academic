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
package org.fenixedu.academic.domain.time.chronologies.durationFields;

import org.fenixedu.academic.domain.time.calendarStructure.AcademicCalendarEntry;
import org.fenixedu.academic.domain.time.chronologies.AcademicChronology;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.field.BaseDurationField;
import org.joda.time.field.FieldUtils;

public class AcademicSemestersDurationField extends BaseDurationField {

    private final AcademicChronology chronology;

    public AcademicSemestersDurationField(AcademicChronology chronology_) {
        super(AcademicSemestersDurationFieldType.academicSemesters());
        this.chronology = chronology_;
    }

    private UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException(
                AcademicSemestersDurationFieldType.academicSemesters() + " field is unsupported");
    }

    @Override
    public long add(long instant, int value) {
        int academicSemester = chronology.getAcademicSemester(instant);
        if (academicSemester != 0) {
            AcademicCalendarEntry academicSemesterCE = chronology.getAcademicSemesterIn(academicSemester);
            AcademicCalendarEntry academicSemesterCEAfter = chronology.getAcademicSemesterIn(academicSemester + value);
            if (academicSemesterCEAfter != null) {
                long result = academicSemesterCEAfter.getBegin().getMillis()
                        + new Duration(academicSemesterCE.getBegin().getMillis(), instant).getMillis();
                return result < academicSemesterCE.getEnd().getMillis() ? result : academicSemesterCE.getEnd().getMillis();
            }
        }
        throw unsupported();
    }

    @Override
    public long add(long instant, long value) {
        return add(instant, FieldUtils.safeToInt(value));
    }

    @Override
    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        int minuendAcademicSemester = chronology.getAcademicSemester(minuendInstant);
        int subtrahendAcademicSemester = chronology.getAcademicSemester(subtrahendInstant);
        if (minuendAcademicSemester != 0 && subtrahendAcademicSemester != 0) {
            return minuendAcademicSemester - subtrahendAcademicSemester;
        }
        throw unsupported();
    }

    @Override
    public long getMillis(int value, long instant) {
        int academicSemester = chronology.getAcademicSemester(instant);
        if (academicSemester != 0) {
            AcademicCalendarEntry academicSemesterCE = chronology.getAcademicSemesterIn(academicSemester);
            AcademicCalendarEntry academicSemesterCEAfter = chronology.getAcademicSemesterIn(academicSemester + value);
            if (academicSemesterCEAfter != null) {
                long result = academicSemesterCEAfter.getBegin().getMillis()
                        + new Duration(academicSemesterCE.getBegin().getMillis(), instant).getMillis();
                return result < academicSemesterCE.getEnd().getMillis() ? result
                        - instant : academicSemesterCE.getEnd().getMillis() - instant;
            }
        }
        throw unsupported();
    }

    @Override
    public long getMillis(long value, long instant) {
        return getMillis(FieldUtils.safeToInt(value), instant);
    }

    @Override
    public long getUnitMillis() {
        return DurationFieldType.years().getField(chronology).getUnitMillis() / 2;
    }

    @Override
    public long getValueAsLong(long duration, long instant) {
        int valueBegin = chronology.getAcademicSemester(instant);
        int valueEnd = chronology.getAcademicSemester(instant + duration);
        if (valueBegin != 0 && valueEnd != 0) {
            return valueEnd - valueBegin;
        }
        throw unsupported();
    }

    @Override
    public boolean isPrecise() {
        return false;
    }
}
