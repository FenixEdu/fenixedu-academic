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
package org.fenixedu.academic.util;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.joda.time.DateTime;

public enum Month {

    JANUARY(1), FEBRUARY(2), MARCH(3), APRIL(4), MAY(5), JUNE(6), JULY(7), AUGUST(8), SEPTEMBER(9), OCTOBER(10), NOVEMBER(11),
    DECEMBER(12);

    private int numberOfMonth;

    private Month(int num) {
        numberOfMonth = num;
    }

    public String getName() {
        return name();
    }

    public int getNumberOfMonth() {
        return numberOfMonth;
    }

    public static Month fromDateTime(DateTime time) {
        return Month.values()[time.getMonthOfYear() - 1];
    }

    public static Month fromInt(int num) {
        return Month.values()[num - 1];
    }

    public static List<Month> getIntervalMonths(final ExecutionInterval interval) {

        int monthStart = interval.getAcademicInterval().getStart().getMonthOfYear();
        int monthEnd = interval.getAcademicInterval().getEnd().getMonthOfYear();

        if (monthStart > monthEnd) {
            monthEnd += 12;
        }

        ArrayList<Month> result = new ArrayList<Month>();

        for (int i = monthStart; i <= monthEnd; i++) {
            result.add(Month.fromInt(i > 12 ? i - 12 : i));
        }

        return result;
    }
}
