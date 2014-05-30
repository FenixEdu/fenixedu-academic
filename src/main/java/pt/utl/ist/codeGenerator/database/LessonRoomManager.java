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
package pt.utl.ist.codeGenerator.database;

import java.util.Stack;

import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.fenixedu.spaces.domain.Space;

public class LessonRoomManager extends Stack<Space> {

    private int nextWeekDay = 2;

    public int getNextWeekDay() {
        return nextWeekDay;
    }

    private HourMinuteSecond nextHourMinuteSecond = new HourMinuteSecond(8, 0, 0);
    private HourMinuteSecond limitHourMinuteSecond = new HourMinuteSecond(20, 0, 0);

    public HourMinuteSecond getNextHourMinuteSecond(final int durationInMinutes) {
        final HourMinuteSecond nextHourMinuteSecond = this.nextHourMinuteSecond;
        if (nextHourMinuteSecond.isAfter(limitHourMinuteSecond)) {
            this.nextHourMinuteSecond = new HourMinuteSecond(8, 0, 0);
            if (nextWeekDay == 7) {
                this.nextWeekDay = 2;
                pop();
            } else {
                this.nextWeekDay = nextWeekDay + 1;
            }
        } else {
            this.nextHourMinuteSecond = nextHourMinuteSecond.plusMinutes(durationInMinutes);
        }
        return nextHourMinuteSecond;
    }

    public Space getNextOldRoom() {
        return peek();
    }

}
