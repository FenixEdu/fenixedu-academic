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
package net.sourceforge.fenixedu.util;

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
}
