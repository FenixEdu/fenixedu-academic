package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;

public class MergeRooms extends FenixService {

	public void run(AllocatableSpace fromRoom, AllocatableSpace destinationRoom) {
		AllocatableSpace.mergeAllocatableSpaces(fromRoom, destinationRoom);
	}
}
