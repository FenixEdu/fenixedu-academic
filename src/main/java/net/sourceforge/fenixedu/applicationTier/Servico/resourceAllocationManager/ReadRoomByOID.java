package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class ReadRoomByOID {

    @Service
    public static InfoRoom run(String oid) {
        final AllocatableSpace room = (AllocatableSpace) FenixFramework.getDomainObject(oid);
        return InfoRoom.newInfoFromDomain(room);
    }

}