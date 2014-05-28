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
import net.sourceforge.fenixedu.domain.time.chronologies.durationFields.AcademicTrimestersDurationFieldType;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;

public class AcademicTrimesterDateTimeFieldType extends DateTimeFieldType {

    private static final AcademicTrimesterDateTimeFieldType ACADEMIC_TRIMESTER_TYPE;
    static {
        ACADEMIC_TRIMESTER_TYPE = new AcademicTrimesterDateTimeFieldType("academicSemester");
    }

    private AcademicTrimesterDateTimeFieldType(String name) {
        super(name);
    }

    public static DateTimeFieldType academicTrimester() {
        return ACADEMIC_TRIMESTER_TYPE;
    }

    @Override
    public DurationFieldType getDurationType() {
        return AcademicTrimestersDurationFieldType.academicTrimesters();
    }

    @Override
    public DateTimeField getField(Chronology chronology) {
        if (chronology instanceof AcademicChronology) {
            return ((AcademicChronology) chronology).academicYear();
        }
        throw unsupported();
    }

    @Override
    public DurationFieldType getRangeDurationType() {
        return null;
    }

    private UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException(ACADEMIC_TRIMESTER_TYPE.getName() + " field is unsupported");
    }

}
