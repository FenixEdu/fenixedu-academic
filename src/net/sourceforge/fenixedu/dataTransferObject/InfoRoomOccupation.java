/*
 * Created on 31/Out/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.util.DiaSemana;

/**
 * @author Ana e Ricardo
 *  
 */
public class InfoRoomOccupation extends InfoObject {

    private DomainReference<RoomOccupation> roomOccupationDomainReference;
    
    public InfoRoomOccupation(final RoomOccupation roomOccupation) {
	roomOccupationDomainReference = new DomainReference<RoomOccupation>(roomOccupation);
    }
    
    public static InfoRoomOccupation newInfoFromDomain(final RoomOccupation roomOccupation) {
        return roomOccupation == null ? null : new InfoRoomOccupation(roomOccupation);
    }
    
    private RoomOccupation getRoomOccupation() {
	return roomOccupationDomainReference == null ? null : roomOccupationDomainReference.getObject();
    }
    
    public Integer getFrequency() {
        return getRoomOccupation().getFrequency();
    }

    public Integer getWeekOfQuinzenalStart() {
        return getRoomOccupation().getWeekOfQuinzenalStart();
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
     * @return Returns the infoPeriod.
     */
    public InfoPeriod getInfoPeriod() {
        return InfoPeriod.newInfoFromDomain(getRoomOccupation().getPeriod());
    }

    /**
     * @return Returns the infoRoom.
     */
    public InfoRoom getInfoRoom() {
        return InfoRoom.newInfoFromDomain((OldRoom) getRoomOccupation().getRoom());
    }

}
