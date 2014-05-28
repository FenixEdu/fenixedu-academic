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
package net.sourceforge.fenixedu.dataTransferObject.spaceManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Lesson;

import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class ViewEventSpaceOccupationsBean implements Serializable {

    private Partial year;

    private Partial month;

    private YearMonthDay day;

    private Space allocatableSpaceReference;

    public static int MONDAY_IN_JODA_TIME = 1;
    public static int SATURDAY_IN_JODA_TIME = 6;

    public ViewEventSpaceOccupationsBean(YearMonthDay day, Space allocatableSpace) {

        setAllocatableSpace(allocatableSpace);

        if (day != null) {
            setYear(new Partial(DateTimeFieldType.year(), day.getYear()));
            setMonth(new Partial(DateTimeFieldType.monthOfYear(), day.getMonthOfYear()));

            YearMonthDay monday = day.toDateTimeAtMidnight().withDayOfWeek(MONDAY_IN_JODA_TIME).toYearMonthDay();
            if ((monday.getMonthOfYear() < day.getMonthOfYear()) || (monday.getYear() < day.getYear())) {
                monday = monday.plusDays(Lesson.NUMBER_OF_DAYS_IN_WEEK);
            }

            setDay(monday);
        }
    }

    public Space getAllocatableSpace() {
        return allocatableSpaceReference;
    }

    public void setAllocatableSpace(Space space) {
        this.allocatableSpaceReference = space;
    }

    public Partial getYear() {
        return year;
    }

    public void setYear(Partial year) {
        this.year = year;
    }

    public Partial getMonth() {
        return month;
    }

    public void setMonth(Partial month) {
        this.month = month;
    }

    public YearMonthDay getDay() {
        return day;
    }

    public void setDay(YearMonthDay firstDayOWeek) {
        this.day = firstDayOWeek;
    }
}
