package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class LerSalas extends FenixService {

	@Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
	@Service
	public static Object run() {
		final List<InfoRoom> infoSalas = new ArrayList<InfoRoom>();
		for (final AllocatableSpace room : AllocatableSpace.getAllActiveAllocatableSpacesForEducation()) {
			infoSalas.add(InfoRoom.newInfoFromDomain(room));
		}
		return infoSalas;
	}
}