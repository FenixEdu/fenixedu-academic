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
/*
 * Created on 31/Out/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.space.EventSpaceOccupation;
import net.sourceforge.fenixedu.util.DiaSemana;

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
