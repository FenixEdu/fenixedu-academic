package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.sop.RoomsPunctualSchedulingBean;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;

public class EditRoomsPunctualScheduling extends Service {

    public void run(RoomsPunctualSchedulingBean bean) {
	
	List<OldRoom> roomsToInsert = bean.getRooms();
	GenericEvent genericEvent = bean.getGenericEvent();
	
	if (genericEvent != null && !roomsToInsert.isEmpty()) {	    
	    
	    List<RoomOccupation> eventRoomOccupations = genericEvent.getRoomOccupations();	    
	    List<RoomOccupation> roomOccupationsToDelete = new ArrayList<RoomOccupation>();
	    	    
	    for (RoomOccupation occupation : eventRoomOccupations) {
		if(!roomsToInsert.contains(occupation.getRoom())) {
		    roomOccupationsToDelete.add(occupation);
		} else {
		    roomsToInsert.remove(occupation.getRoom());
		}
	    }	
	    
	    genericEvent.edit(bean.getSmallDescription(), bean.getCompleteDescription(), roomsToInsert, roomOccupationsToDelete);	    
	}
    }    
}
