/*
 * Created on 31/Out/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.space.EventSpaceOccupation;
import net.sourceforge.fenixedu.util.DiaSemana;

/**
 * @author Ana e Ricardo
 * 
 */
public class InfoRoomOccupation extends InfoObject {

    private DomainReference<EventSpaceOccupation> roomOccupationDomainReference;

    public InfoRoomOccupation(final EventSpaceOccupation roomOccupation) {
	roomOccupationDomainReference = new DomainReference<EventSpaceOccupation>(roomOccupation);
    }

    public static InfoRoomOccupation newInfoFromDomain(final EventSpaceOccupation roomOccupation) {
	return roomOccupation == null ? null : new InfoRoomOccupation(roomOccupation);
    }

    private EventSpaceOccupation getRoomOccupation() {
	return roomOccupationDomainReference == null ? null : roomOccupationDomainReference.getObject();
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
