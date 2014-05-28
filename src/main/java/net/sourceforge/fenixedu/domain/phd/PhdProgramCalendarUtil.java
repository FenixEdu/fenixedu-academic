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
package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.Holiday;
import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.LocalDate;

public class PhdProgramCalendarUtil {

    static public int countWorkDaysBetween(final LocalDate startDate, final LocalDate endDate) {

        int result = 0;
        LocalDate current = startDate.plusDays(1);

        while (!current.isAfter(endDate)) {
            if (isWorkDay(current)) {
                result++;
            }

            current = current.plusDays(1);
        }

        return result;
    }

    static public LocalDate addWorkDaysTo(final LocalDate date, final int workDays) {
        int current = workDays;
        LocalDate result = date;

        while (current > 0) {
            result = result.plusDays(1);

            if (isWorkDay(result)) {
                current--;
            }
        }

        return result;
    }

    static private boolean isWorkDay(LocalDate current) {
        final WeekDay dayOfWeek = WeekDay.fromJodaTimeToWeekDay(current.toDateTimeAtStartOfDay());

        return dayOfWeek != WeekDay.SATURDAY && dayOfWeek != WeekDay.SUNDAY && !Holiday.isHoliday(current);
    }
}
