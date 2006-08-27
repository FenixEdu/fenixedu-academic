/*
 * Created on 29/Abr/2005 - 12:04:24
 * 
 */

package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.space.OldRoom;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class InfoRoomWithInfoInquiriesRoom extends InfoRoom {

    private InfoInquiriesRoom inquiriesRoom;

    public static InfoRoomWithInfoInquiriesRoom newInfoFromDomain(OldRoom room) {
	InfoRoomWithInfoInquiriesRoom infoRoom = null;
	if (room != null) {
	    infoRoom = new InfoRoomWithInfoInquiriesRoom(room);
	}
	return infoRoom;
    }

    public InfoRoomWithInfoInquiriesRoom(final OldRoom oldRoom) {
	super(oldRoom);
    }

    public InfoInquiriesRoom getInquiriesRoom() {
	return inquiriesRoom;
    }

    public void setInquiriesRoom(InfoInquiriesRoom inquiriesRoom) {
	this.inquiriesRoom = inquiriesRoom;
    }

}
