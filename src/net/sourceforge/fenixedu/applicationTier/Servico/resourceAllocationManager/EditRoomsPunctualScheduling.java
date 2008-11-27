package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.RoomsPunctualSchedulingBean;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.GenericEventSpaceOccupation;

public class EditRoomsPunctualScheduling extends FenixService {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static void run(RoomsPunctualSchedulingBean bean) {

	List<AllocatableSpace> roomsToInsert = bean.getRooms();
	GenericEvent genericEvent = bean.getGenericEvent();

	if (genericEvent != null && !roomsToInsert.isEmpty()) {

	    List<GenericEventSpaceOccupation> eventRoomOccupations = genericEvent.getGenericEventSpaceOccupations();
	    List<GenericEventSpaceOccupation> roomOccupationsToDelete = new ArrayList<GenericEventSpaceOccupation>();

	    for (GenericEventSpaceOccupation occupation : eventRoomOccupations) {
		if (!roomsToInsert.contains(occupation.getRoom())) {
		    roomOccupationsToDelete.add(occupation);
		} else {
		    roomsToInsert.remove(occupation.getRoom());
		}
	    }

	    genericEvent.edit(bean.getSmallDescription(), bean.getCompleteDescription(), roomsToInsert, roomOccupationsToDelete);
	}
    }
}