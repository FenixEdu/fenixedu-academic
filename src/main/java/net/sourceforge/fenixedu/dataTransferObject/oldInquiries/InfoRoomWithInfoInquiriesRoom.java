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
 * Created on 29/Abr/2005 - 12:04:24
 * 
 */

package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;

import org.fenixedu.spaces.domain.Space;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class InfoRoomWithInfoInquiriesRoom extends InfoRoom {

    private InfoInquiriesRoom inquiriesRoom;

    public static InfoRoomWithInfoInquiriesRoom newInfoFromDomain(Space room) {
        InfoRoomWithInfoInquiriesRoom infoRoom = null;
        if (room != null) {
            infoRoom = new InfoRoomWithInfoInquiriesRoom(room);
        }
        return infoRoom;
    }

    public InfoRoomWithInfoInquiriesRoom(final Space oldRoom) {
        super(oldRoom);
    }

    public InfoInquiriesRoom getInquiriesRoom() {
        return inquiriesRoom;
    }

    public void setInquiriesRoom(InfoInquiriesRoom inquiriesRoom) {
        this.inquiriesRoom = inquiriesRoom;
    }

}
