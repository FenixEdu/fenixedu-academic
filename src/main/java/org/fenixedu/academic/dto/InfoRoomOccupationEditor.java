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

import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.util.DiaSemana;

public class InfoRoomOccupationEditor extends InfoObject {

    protected Calendar startTime;

    protected Calendar endTime;

    protected DiaSemana dayOfWeek;

    protected InfoRoom infoRoom;

    protected InfoPeriod infoPeriod;

    protected FrequencyType frequency;

    protected Integer weekOfQuinzenalStart;

    public FrequencyType getFrequency() {
        return frequency;
    }

    public void setFrequency(FrequencyType frequency) {
        this.frequency = frequency;
    }

    public Integer getWeekOfQuinzenalStart() {
        return weekOfQuinzenalStart;
    }

    public void setWeekOfQuinzenalStart(Integer weekOfQuinzenalStart) {
        this.weekOfQuinzenalStart = weekOfQuinzenalStart;
    }

    public DiaSemana getDayOfWeek() {
        return dayOfWeek;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setDayOfWeek(DiaSemana semana) {
        dayOfWeek = semana;
    }

    public void setEndTime(Calendar calendar) {
        endTime = calendar;
    }

    public void setStartTime(Calendar calendar) {
        startTime = calendar;
    }

    public InfoPeriod getInfoPeriod() {
        return infoPeriod;
    }

    public void setInfoPeriod(InfoPeriod infoPeriod) {
        this.infoPeriod = infoPeriod;
    }

    public InfoRoom getInfoRoom() {
        return infoRoom;
    }

    public void setInfoRoom(InfoRoom infoRoom) {
        this.infoRoom = infoRoom;
    }

}
