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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.time.AcademicPeriodType;
import org.fenixedu.academic.domain.time.chronologies.durationFields.AcademicYearsDurationFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;

public class AcademicYears extends AcademicPeriod {

    protected AcademicYears(int period, String name) {
        super(period, name);
    }

    @Override
    public DurationFieldType getFieldType() {
        return AcademicYearsDurationFieldType.academicYears();
    }

    @Override
    public PeriodType getPeriodType() {
        return AcademicPeriodType.academicYears();
    }

    @Override
    public float getWeight() {
        return getValue();
    }

    @Override
    public AcademicPeriod getPossibleChild() {
        return getValue() > 1 ? AcademicPeriod.YEAR : AcademicPeriod.SEMESTER;
    }

    @Override
    public Collection<AcademicPeriod> getPossibleChilds() {
        final Set<AcademicPeriod> result = new HashSet<>();

        if (getValue() > 1) {
            result.add(AcademicPeriod.YEAR);
        } else {
            result.add(AcademicPeriod.SEMESTER);
            result.add(AcademicPeriod.TRIMESTER);
            result.add(AcademicPeriod.MONTH);
            result.add(AcademicPeriod.WEEK);
            result.add(AcademicPeriod.DAY);
        }

        return result;
    }

}
