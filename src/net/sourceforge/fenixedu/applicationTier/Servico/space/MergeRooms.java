package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;

public class MergeRooms extends Service {

    public void run(AllocatableSpace fromRoom, AllocatableSpace destinationRoom) {
	AllocatableSpace.mergeAllocatableSpaces(fromRoom, destinationRoom);		
    }    
}
