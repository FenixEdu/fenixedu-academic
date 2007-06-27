package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoRoomWithInfoInquiriesRoom;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.RoomClassification;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class SearchRooms extends Service {

    public List run(String name, String building, Integer floor, RoomClassification type, Integer normal,
	    Integer exam) throws FenixServiceException, ExcepcaoPersistencia {

	final List<AllocatableSpace> rooms = AllocatableSpace.findActiveAllocatableSpacesBySpecifiedArguments(name, building, floor, type, normal, exam);
	final List<InfoRoom> infoRooms = new ArrayList();
	for (final AllocatableSpace room : rooms) {
	    if(room.containsIdentification()) {
		infoRooms.add(InfoRoomWithInfoInquiriesRoom.newInfoFromDomain(room));
	    }
	}
	return infoRooms;
    }
}