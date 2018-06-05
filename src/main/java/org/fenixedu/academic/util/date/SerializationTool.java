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
package org.fenixedu.academic.util.date;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class SerializationTool {

    public static String yearMonthDaySerialize(final YearMonthDay yearMonthDay) {
        if (yearMonthDay != null) {
            final String dateString = String.format("%04d-%02d-%02d", yearMonthDay.get(DateTimeFieldType.year()),
                    yearMonthDay.get(DateTimeFieldType.monthOfYear()), yearMonthDay.get(DateTimeFieldType.dayOfMonth()));
            return dateString;
        }
        return null;
    }

    public static YearMonthDay yearMonthDayDeserialize(final String string) {
        if (StringUtils.isBlank(string)) {
            return null;
        }

        String[] tokens = string.split("\\-");
        if (tokens.length != 3) {
            System.err.println("Something went wrong with this object. Will be set to null - " + string);
            return null;
        }

        int year = Integer.parseInt(tokens[0]);
        int month = Integer.parseInt(tokens[1]);
        int day = Integer.parseInt(tokens[2]);

        return year == 0 || month == 0 || day == 0 ? null : new YearMonthDay(year, month, day);
    }

    public static String intervalSerialize(final Interval interval) {
        return interval.toString();
    }

    public static Interval intervalDeserialize(final String string) {
        if (!StringUtils.isEmpty(string)) {
            String[] parts = string.split("/");
            DateTime start = new DateTime(parts[0]);
            DateTime end = new DateTime(parts[1]);
            return new Interval(start, end);
        }
        return null;
    }
}
