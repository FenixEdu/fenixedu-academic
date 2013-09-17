package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.RoomsPunctualSchedulingBean;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.GenericEventSpaceOccupation;

import org.apache.struts.util.MessageResources;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class EditRoomsPunctualScheduling {

    public static final MessageResources messages = MessageResources
            .getMessageResources("resources/ResourceAllocationManagerResources");

    @Atomic
    public static void run(RoomsPunctualSchedulingBean bean) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);

        List<AllocatableSpace> roomsToInsert = bean.getRooms();
        GenericEvent genericEvent = bean.getGenericEvent();

        if (genericEvent != null && !roomsToInsert.isEmpty()) {

            Collection<GenericEventSpaceOccupation> eventRoomOccupations = genericEvent.getGenericEventSpaceOccupations();
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