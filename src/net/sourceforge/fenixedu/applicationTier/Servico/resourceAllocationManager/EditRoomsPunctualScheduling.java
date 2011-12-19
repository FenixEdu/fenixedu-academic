package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.RoomsPunctualSchedulingBean;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.GenericEventSpaceOccupation;

import org.apache.struts.util.MessageResources;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class EditRoomsPunctualScheduling extends FenixService {

    public static final MessageResources messages = MessageResources
	    .getMessageResources("resources/ResourceAllocationManagerResources");

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