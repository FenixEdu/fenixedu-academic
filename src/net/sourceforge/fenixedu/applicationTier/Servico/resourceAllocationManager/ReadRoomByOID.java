package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadRoomByOID extends Service {

    public InfoRoom run(Integer oid) throws ExcepcaoPersistencia {
	final AllocatableSpace room = (AllocatableSpace) rootDomainObject.readResourceByOID(oid);
	return InfoRoom.newInfoFromDomain(room);
    }

}