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

import net.sourceforge.fenixedu.domain.time.AcademicPeriodType;
import net.sourceforge.fenixedu.domain.time.chronologies.durationFields.AcademicTrimestersDurationFieldType;

import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;

public class AcademicTrimesters extends AcademicPeriod {

    protected AcademicTrimesters(int period, String name) {
        super(period, name);
    }

    @Override
    public float getWeight() {
        return getValue() / 4f;
    }

    @Override
    public DurationFieldType getFieldType() {
        return AcademicTrimestersDurationFieldType.academicTrimesters();
    }

    @Override
    public PeriodType getPeriodType() {
        return AcademicPeriodType.academicTrimesters();
    }

    /**
     * For now, nothing smaller than a trimester is supported
     */
    @Override
    public AcademicPeriod getPossibleChild() {
        return null;
    }

}
