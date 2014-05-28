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
package net.sourceforge.fenixedu.domain.time.chronologies.dateTimeFields;

import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;
import net.sourceforge.fenixedu.domain.time.chronologies.durationFields.AcademicSemestersDurationFieldType;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;

public class AcademicSemesterDateTimeFieldType extends DateTimeFieldType {

    private static final AcademicSemesterDateTimeFieldType ACADEMIC_SEMESTER_TYPE;
    static {
        ACADEMIC_SEMESTER_TYPE = new AcademicSemesterDateTimeFieldType("academicSemester");
    }

    protected AcademicSemesterDateTimeFieldType(String name) {
        super(name);
    }

    public static DateTimeFieldType academicSemester() {
        return ACADEMIC_SEMESTER_TYPE;
    }

    @Override
    public DurationFieldType getDurationType() {
        return AcademicSemestersDurationFieldType.academicSemesters();
    }

    @Override
    public DateTimeField getField(Chronology chronology) {
        if (chronology instanceof AcademicChronology) {
            return ((AcademicChronology) chronology).academicSemester();
        }
        throw unsupported();
    }

    @Override
    public DurationFieldType getRangeDurationType() {
        return null;
    }

    private UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException(ACADEMIC_SEMESTER_TYPE + " field is unsupported");
    }
}
