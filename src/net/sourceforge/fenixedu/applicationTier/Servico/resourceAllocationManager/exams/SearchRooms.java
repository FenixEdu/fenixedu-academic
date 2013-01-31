package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.oldInquiries.InfoRoomWithInfoInquiriesRoom;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.RoomClassification;
import pt.ist.fenixWebFramework.services.Service;

public class SearchRooms extends FenixService {

	@Service
	public static List run(String name, String building, Integer floor, RoomClassification type, Integer normal, Integer exam)
			throws FenixServiceException {

		final List<AllocatableSpace> rooms =
				AllocatableSpace.findActiveAllocatableSpacesBySpecifiedArguments(name, building, floor, type, normal, exam);
		final List<InfoRoom> infoRooms = new ArrayList();
		for (final AllocatableSpace room : rooms) {
			if (room.containsIdentification()) {
				infoRooms.add(InfoRoomWithInfoInquiriesRoom.newInfoFromDomain(room));
			}
		}
		return infoRooms;
	}
}