package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class LerSalas extends Service {

    public Object run() {
	final List<InfoRoom> infoSalas = new ArrayList<InfoRoom>();
	for (final AllocatableSpace room : AllocatableSpace.getAllActiveAllocatableSpacesForEducation()) {
	    infoSalas.add(InfoRoom.newInfoFromDomain(room));
	}
	return infoSalas;
    }
}