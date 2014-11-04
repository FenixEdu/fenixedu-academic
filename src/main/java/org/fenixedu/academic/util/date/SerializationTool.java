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
package net.sourceforge.fenixedu.util.date;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class SerializationTool {

    public static String yearMonthDaySerialize(final YearMonthDay yearMonthDay) {
        if (yearMonthDay != null) {
            final String dateString =
                    String.format("%d-%02d-%02d", yearMonthDay.get(DateTimeFieldType.year()),
                            yearMonthDay.get(DateTimeFieldType.monthOfYear()), yearMonthDay.get(DateTimeFieldType.dayOfMonth()));
            return dateString.length() != 10 ? null : dateString;
        }
        return null;
    }

    public static YearMonthDay yearMonthDayDeserialize(String string) {
        if (!StringUtils.isEmpty(string)) {
            int year = Integer.parseInt(string.substring(0, 4));
            int month = Integer.parseInt(string.substring(5, 7));
            int day = Integer.parseInt(string.substring(8, 10));
            return year == 0 || month == 0 || day == 0 ? null : new YearMonthDay(year, month, day);
        }
        return null;
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
