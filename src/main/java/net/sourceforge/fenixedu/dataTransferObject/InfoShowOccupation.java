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
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.fenixedu.spaces.domain.Space;

/**
 * @author Ana e Ricardo
 *
 *         19/7/2004
 */

public abstract class InfoShowOccupation extends InfoObject implements ISiteComponent {

    public abstract InfoShift getInfoShift();

    public abstract ShiftType getTipo();

    public abstract InfoRoomOccupation getInfoRoomOccupation();

    public abstract DiaSemana getDiaSemana();

    public abstract Calendar getInicio();

    public abstract Calendar getFim();

    public int getFirstHourOfDay() {
        final Calendar start = getInicio();
        return start.get(Calendar.HOUR_OF_DAY);
    }

    public int getLastHourOfDay() {
        final Calendar end = getFim();
        final int endHour = end.get(Calendar.HOUR_OF_DAY);
        return end.get(Calendar.MINUTE) > 0 ? endHour + 1 : endHour;
    }

    public HourMinuteSecond getBeginHourMinuteSecond() {
        final Calendar start = getInicio();
        return new HourMinuteSecond(start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE), start.get(Calendar.SECOND));
    }

    public HourMinuteSecond getEndHourMinuteSecond() {
        final Calendar end = getFim();
        return new HourMinuteSecond(end.get(Calendar.HOUR_OF_DAY), end.get(Calendar.MINUTE), end.get(Calendar.SECOND));
    }

    public Space getAllocatableSpace() {
        final InfoRoomOccupation infoOccupation = getInfoRoomOccupation();
        final InfoRoom infoRoom = infoOccupation == null ? null : infoOccupation.getInfoRoom();
        return infoRoom == null ? null : infoRoom.getRoom();
    }

}
