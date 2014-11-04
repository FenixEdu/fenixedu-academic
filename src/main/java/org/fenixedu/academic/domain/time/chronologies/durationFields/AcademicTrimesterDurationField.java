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

import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

public class AcademicTrimesterDurationField extends DurationField {

    private final Chronology chronology;

    public AcademicTrimesterDurationField(Chronology chronology) {
        super();
        this.chronology = chronology;
    }

    @Override
    public long add(long instant, int value) {
        throw unsupported();
    }

    @Override
    public long add(long instant, long value) {
        throw unsupported();
    }

    @Override
    public int compareTo(DurationField durationField) {
        throw unsupported();
    }

    @Override
    public int getDifference(long minuendInstant, long subtrahendInstant) {
        throw unsupported();
    }

    @Override
    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        throw unsupported();
    }

    @Override
    public long getMillis(int value) {
        throw unsupported();
    }

    @Override
    public long getMillis(long value) {
        throw unsupported();
    }

    @Override
    public long getMillis(int value, long instant) {
        throw unsupported();
    }

    @Override
    public long getMillis(long value, long instant) {
        throw unsupported();
    }

    @Override
    public String getName() {
        return AcademicTrimestersDurationFieldType.academicTrimesters().getName();
    }

    @Override
    public DurationFieldType getType() {
        return AcademicTrimestersDurationFieldType.academicTrimesters();
    }

    @Override
    public long getUnitMillis() {
        throw unsupported();
    }

    @Override
    public int getValue(long duration) {
        throw unsupported();
    }

    @Override
    public int getValue(long duration, long instant) {
        throw unsupported();
    }

    @Override
    public long getValueAsLong(long duration) {
        throw unsupported();
    }

    @Override
    public long getValueAsLong(long duration, long instant) {
        throw unsupported();
    }

    @Override
    public boolean isPrecise() {
        return false;
    }

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public String toString() {
        throw unsupported();
    }

    private UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException(AcademicTrimestersDurationFieldType.academicTrimesters()
                + " field is unsupported");
    }

}
