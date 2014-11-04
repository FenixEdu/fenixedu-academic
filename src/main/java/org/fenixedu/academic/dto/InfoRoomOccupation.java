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
/*
 * Created on 31/Out/2003
 *
 */
package org.fenixedu.academic.dto;

import java.util.Calendar;

import org.fenixedu.academic.domain.FrequencyType;
import org.fenixedu.academic.domain.space.EventSpaceOccupation;
import org.fenixedu.academic.util.DiaSemana;

/**
 * @author Ana e Ricardo
 * 
 */
public class InfoRoomOccupation extends InfoObject {

    private EventSpaceOccupation roomOccupationDomainReference;

    public InfoRoomOccupation(final EventSpaceOccupation roomOccupation) {
        roomOccupationDomainReference = roomOccupation;
    }

    public static InfoRoomOccupation newInfoFromDomain(final EventSpaceOccupation roomOccupation) {
        return roomOccupation == null ? null : new InfoRoomOccupation(roomOccupation);
    }

    private EventSpaceOccupation getRoomOccupation() {
        return roomOccupationDomainReference;
    }

    public FrequencyType getFrequency() {
        return getRoomOccupation().getFrequency();
    }

    /**
     * @return
     */
    public DiaSemana getDayOfWeek() {
        return getRoomOccupation().getDayOfWeek();
    }

    /**
     * @return
     */
    public Calendar getEndTime() {
        return getRoomOccupation().getEndTime();
    }

    /**
     * @return
     */
    public Calendar getStartTime() {
        return getRoomOccupation().getStartTime();
    }

    /**
     * @return Returns the infoRoom.
     */
    public InfoRoom getInfoRoom() {
        return InfoRoom.newInfoFromDomain(getRoomOccupation().getRoom());
    }

}
