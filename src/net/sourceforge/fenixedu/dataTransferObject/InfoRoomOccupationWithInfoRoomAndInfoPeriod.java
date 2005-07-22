/*
 * Created on 2005/05/11
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IRoomOccupation;

/**
 * 
 * @author Luis Cruz 
 */
public class InfoRoomOccupationWithInfoRoomAndInfoPeriod extends InfoRoomOccupationWithInfoRoom {

    @Override
    public void copyFromDomain(IRoomOccupation roomOccupation) {
        super.copyFromDomain(roomOccupation);
        if (roomOccupation != null) {
            setInfoPeriod(InfoPeriod.newInfoFromDomain(roomOccupation.getPeriod()));
        }
    }

    public static InfoRoomOccupation newInfoFromDomain(IRoomOccupation roomOccupation) {
        InfoRoomOccupationWithInfoRoom infoRoomOccupation = null;
        if (roomOccupation != null) {
            infoRoomOccupation = new InfoRoomOccupationWithInfoRoom();
            infoRoomOccupation.copyFromDomain(roomOccupation);
        }
        return infoRoomOccupation;
    }

}