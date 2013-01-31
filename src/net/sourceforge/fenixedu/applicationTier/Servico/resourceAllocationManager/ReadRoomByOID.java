package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import pt.ist.fenixWebFramework.services.Service;

public class ReadRoomByOID extends FenixService {

	@Service
	public static InfoRoom run(Integer oid) {
		final AllocatableSpace room = (AllocatableSpace) rootDomainObject.readResourceByOID(oid);
		return InfoRoom.newInfoFromDomain(room);
	}

}