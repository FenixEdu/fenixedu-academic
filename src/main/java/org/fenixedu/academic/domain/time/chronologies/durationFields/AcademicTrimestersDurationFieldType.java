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
package net.sourceforge.fenixedu.domain.time.chronologies.durationFields;

import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;

import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

public class AcademicTrimestersDurationFieldType extends DurationFieldType {

    public static final AcademicTrimestersDurationFieldType ACADEMIC_TRIMESTERS_TYPE;
    static {
        ACADEMIC_TRIMESTERS_TYPE = new AcademicTrimestersDurationFieldType("academicTrimesters");
    }

    protected AcademicTrimestersDurationFieldType(String name) {
        super(name);
    }

    public static DurationFieldType academicTrimesters() {
        return ACADEMIC_TRIMESTERS_TYPE;
    }

    @Override
    public DurationField getField(Chronology chronology) {
        if (chronology instanceof AcademicChronology) {
            return ((AcademicChronology) chronology).academicTrimesters();
        }
        return null;
    }

}
