/*
 * Created on 29/Abr/2005 - 12:04:24
 * 
 */

package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import org.fenixedu.spaces.domain.Space;

import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;

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
