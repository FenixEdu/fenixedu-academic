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

import org.fenixedu.spaces.domain.occupation.Occupation;

/**
 * @author Ana e Ricardo
 * 
 */
public class InfoRoomOccupation extends InfoObject {

    private Occupation roomOccupationDomainReference;

    public InfoRoomOccupation(final Occupation roomOccupation) {
        roomOccupationDomainReference = roomOccupation;
    }

    public static InfoRoomOccupation newInfoFromDomain(final Occupation roomOccupation) {
        return roomOccupation == null ? null : new InfoRoomOccupation(roomOccupation);
    }

    private Occupation getRoomOccupation() {
        return roomOccupationDomainReference;
    }

//    public FrequencyType getFrequency() {
//        return getRoomOccupation().getFrequency();
//    }

    /**
     * @return
     */
//    public DiaSemana getDayOfWeek() {
//        return getRoomOccupation().getDayOfWeek();
//    }

//    public Calendar getEndTime() {
//        HourMinuteSecond hms = getRoomOccupation().getEndTimeDateHourMinuteSecond();
//        Date date =
//                (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
//        if (date != null) {
//            Calendar result = Calendar.getInstance();
//            result.setTime(date);
//            return result;
//        }
//        return null;
//    }
//
//    public Calendar getStartTime() {
//        HourMinuteSecond hms = getRoomOccupation().getStartTimeDateHourMinuteSecond();
//        Date date =
//                (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
//        if (date != null) {
//            Calendar result = Calendar.getInstance();
//            result.setTime(date);
//            return result;
//        }
//        return null;
//    }

    /**
     * @return Returns the infoRoom.
     */
    public InfoRoom getInfoRoom() {
        return InfoRoom.newInfoFromDomain(getRoomOccupation().getSpaces().stream().findAny().orElse(null));
    }

}
