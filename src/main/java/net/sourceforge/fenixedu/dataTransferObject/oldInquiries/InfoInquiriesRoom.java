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
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import java.lang.reflect.InvocationTargetException;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.oldInquiries.InquiriesRoom;
import net.sourceforge.fenixedu.util.InquiriesUtil;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class InfoInquiriesRoom extends InfoObject implements Comparable {

    private Integer keyInquiriesCourse;
    private InfoInquiriesCourse inquiriesCourse;

    private Integer keyRoom;
    private InfoRoomWithInfoInquiriesRoom room;

    private Integer spaceAdequation;
    private Integer environmentalConditions;
    private Integer equipmentQuality;

    public InfoInquiriesCourse getInquiriesCourse() {
        return inquiriesCourse;
    }

    public void setInquiriesCourse(InfoInquiriesCourse inquiriesCourse) {
        this.inquiriesCourse = inquiriesCourse;
    }

    public Integer getKeyInquiriesCourse() {
        return keyInquiriesCourse;
    }

    public void setKeyInquiriesCourse(Integer keyInquiriesCourse) {
        this.keyInquiriesCourse = keyInquiriesCourse;
    }

    /**
     * @return Returns the environmentalConditions.
     */
    public Integer getEnvironmentalConditions() {
        return environmentalConditions;
    }

    /**
     * @param environmentalConditions
     *            The environmentalConditions to set.
     */
    public void setEnvironmentalConditions(Integer environmentalConditions) {
        if (InquiriesUtil.isValidAnswer(environmentalConditions)) {
            this.environmentalConditions = environmentalConditions;
        } else {
            this.environmentalConditions = null;
        }
    }

    /**
     * @return Returns the equipmentQuality.
     */
    public Integer getEquipmentQuality() {
        return equipmentQuality;
    }

    /**
     * @param equipmentQuality
     *            The equipmentQuality to set.
     */
    public void setEquipmentQuality(Integer equipmentQuality) {
        if (InquiriesUtil.isValidAnswer(equipmentQuality)) {
            this.equipmentQuality = equipmentQuality;
        } else {
            this.equipmentQuality = null;
        }
    }

    /**
     * @return Returns the keyRoom.
     */
    public Integer getKeyRoom() {
        return keyRoom;
    }

    /**
     * @param keyRoom
     *            The keyRoom to set.
     */
    public void setKeyRoom(Integer keyRoom) {
        this.keyRoom = keyRoom;
    }

    /**
     * @return Returns the room.
     */
    public InfoRoomWithInfoInquiriesRoom getRoom() {
        return room;
    }

    /**
     * @param room
     *            The room to set.
     */
    public void setRoom(InfoRoomWithInfoInquiriesRoom room) {
        this.room = room;
    }

    /**
     * @return Returns the spaceAdequation.
     */
    public Integer getSpaceAdequation() {
        return spaceAdequation;
    }

    /**
     * @param spaceAdequation
     *            The spaceAdequation to set.
     */
    public void setSpaceAdequation(Integer spaceAdequation) {
        if (InquiriesUtil.isValidAnswer(spaceAdequation)) {
            this.spaceAdequation = spaceAdequation;
        } else {
            this.spaceAdequation = null;
        }
    }

    @Override
    public int compareTo(Object arg0) {
        return 0;
    }

    public static InfoInquiriesRoom newInfoFromDomain(InquiriesRoom inquiriesRoom) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        InfoInquiriesRoom newInfo = null;
        if (inquiriesRoom != null) {
            newInfo = new InfoInquiriesRoom();
            newInfo.copyFromDomain(inquiriesRoom);
        }
        return newInfo;
    }

    public void copyFromDomain(InquiriesRoom inquiriesRoom) throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        if (inquiriesRoom != null) {
            super.copyFromDomain(inquiriesRoom);
        }

        this.setEnvironmentalConditions(inquiriesRoom.getEnvironmentalConditions());
        this.setEnvironmentalConditions(inquiriesRoom.getEnvironmentalConditions());
        this.setExternalId(inquiriesRoom.getExternalId());
        this.setSpaceAdequation(inquiriesRoom.getSpaceAdequation());
        this.setInquiriesCourse(InfoInquiriesCourse.newInfoFromDomain(inquiriesRoom.getInquiriesCourse()));
        this.setRoom(InfoRoomWithInfoInquiriesRoom.newInfoFromDomain(inquiriesRoom.getRoom()));
    }

    @Override
    public String toString() {
        String result = "[INFOINQUIRIESROOM";
        result += ", id=" + getExternalId();
        if (room != null) {
            result += ", room=" + room.toString();
        }
        result += ", spaceAdequation=" + spaceAdequation;
        result += ", environmentalConditions=" + environmentalConditions;
        result += ", equipmentQuality=" + equipmentQuality;
        result += "]\n";
        return result;
    }

}
