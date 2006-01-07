/*
 * Created on 8/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.space.RoomOccupation;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoRoomOccupationWithInfoRoom extends InfoRoomOccupation {

    @Override
    public void copyFromDomain(RoomOccupation roomOccupation) {
        super.copyFromDomain(roomOccupation);
        if (roomOccupation != null) {
            setInfoRoom(InfoRoom.newInfoFromDomain(roomOccupation.getRoom()));
        }
    }

    public static InfoRoomOccupation newInfoFromDomain(RoomOccupation roomOccupation) {
        InfoRoomOccupationWithInfoRoom infoRoomOccupation = null;
        if (roomOccupation != null) {
            infoRoomOccupation = new InfoRoomOccupationWithInfoRoom();
            infoRoomOccupation.copyFromDomain(roomOccupation);
        }
        return infoRoomOccupation;
    }
}