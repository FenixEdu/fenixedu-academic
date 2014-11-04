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

import java.util.Locale;

import net.sourceforge.fenixedu.domain.time.chronologies.AcademicChronology;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;

public class AcademicTrimesterDateTimeField extends DateTimeField {

    private final Chronology chronology;

    public AcademicTrimesterDateTimeField(Chronology chronology) {
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
    public int[] add(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
        throw unsupported();
    }

    @Override
    public long addWrapField(long instant, int value) {
        throw unsupported();
    }

    @Override
    public int[] addWrapField(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
        throw unsupported();
    }

    @Override
    public int[] addWrapPartial(ReadablePartial instant, int fieldIndex, int[] values, int valueToAdd) {
        throw unsupported();
    }

    @Override
    public int get(long instant) {
        if (chronology instanceof AcademicChronology) {
            return ((AcademicChronology) chronology).getAcademicTrimester(instant);
        }
        throw unsupported();
    }

    @Override
    public String getAsShortText(long instant) {
        throw unsupported();
    }

    @Override
    public String getAsShortText(long instant, Locale locale) {
        throw unsupported();
    }

    @Override
    public String getAsShortText(ReadablePartial partial, Locale locale) {
        throw unsupported();
    }

    @Override
    public String getAsShortText(int fieldValue, Locale locale) {
        throw unsupported();
    }

    @Override
    public String getAsShortText(ReadablePartial partial, int fieldValue, Locale locale) {
        throw unsupported();
    }

    @Override
    public String getAsText(long instant) {
        throw unsupported();
    }

    @Override
    public String getAsText(long instant, Locale locale) {
        throw unsupported();
    }

    @Override
    public String getAsText(ReadablePartial partial, Locale locale) {
        throw unsupported();
    }

    @Override
    public String getAsText(int fieldValue, Locale locale) {
        throw unsupported();
    }

    @Override
    public String getAsText(ReadablePartial partial, int fieldValue, Locale locale) {
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
    public DurationField getDurationField() {
        throw unsupported();
    }

    @Override
    public int getLeapAmount(long instant) {
        throw unsupported();
    }

    @Override
    public DurationField getLeapDurationField() {
        throw unsupported();
    }

    @Override
    public int getMaximumShortTextLength(Locale locale) {
        throw unsupported();
    }

    @Override
    public int getMaximumTextLength(Locale locale) {
        throw unsupported();
    }

    @Override
    public int getMaximumValue() {
        throw unsupported();
    }

    @Override
    public int getMaximumValue(long instant) {
        throw unsupported();
    }

    @Override
    public int getMaximumValue(ReadablePartial instant) {
        throw unsupported();
    }

    @Override
    public int getMaximumValue(ReadablePartial instant, int[] values) {
        throw unsupported();
    }

    @Override
    public int getMinimumValue() {
        throw unsupported();
    }

    @Override
    public int getMinimumValue(long instant) {
        throw unsupported();
    }

    @Override
    public int getMinimumValue(ReadablePartial instant) {
        throw unsupported();
    }

    @Override
    public int getMinimumValue(ReadablePartial instant, int[] values) {
        throw unsupported();
    }

    @Override
    public String getName() {
        throw unsupported();
    }

    @Override
    public DurationField getRangeDurationField() {
        throw unsupported();
    }

    @Override
    public DateTimeFieldType getType() {
        throw unsupported();
    }

    @Override
    public boolean isLeap(long instant) {
        throw unsupported();
    }

    @Override
    public boolean isLenient() {
        throw unsupported();
    }

    @Override
    public boolean isSupported() {
        throw unsupported();
    }

    @Override
    public long remainder(long instant) {
        throw unsupported();
    }

    @Override
    public long roundCeiling(long instant) {
        throw unsupported();
    }

    @Override
    public long roundFloor(long instant) {
        throw unsupported();
    }

    @Override
    public long roundHalfCeiling(long instant) {
        throw unsupported();
    }

    @Override
    public long roundHalfEven(long instant) {
        throw unsupported();
    }

    @Override
    public long roundHalfFloor(long instant) {
        throw unsupported();
    }

    @Override
    public long set(long instant, int value) {
        throw unsupported();
    }

    @Override
    public long set(long instant, String text) {
        throw unsupported();
    }

    @Override
    public long set(long instant, String text, Locale locale) {
        throw unsupported();
    }

    @Override
    public int[] set(ReadablePartial instant, int fieldIndex, int[] values, int newValue) {
        throw unsupported();
    }

    @Override
    public int[] set(ReadablePartial instant, int fieldIndex, int[] values, String text, Locale locale) {
        throw unsupported();
    }

    @Override
    public String toString() {
        throw unsupported();
    }

    private UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException(AcademicTrimesterDateTimeFieldType.academicTrimester() + " field is unsupported");
    }

}
