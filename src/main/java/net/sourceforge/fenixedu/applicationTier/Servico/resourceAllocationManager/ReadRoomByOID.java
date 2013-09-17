package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadRoomByOID {

    @Atomic
    public static InfoRoom run(String oid) {
        final AllocatableSpace room = (AllocatableSpace) FenixFramework.getDomainObject(oid);
        return InfoRoom.newInfoFromDomain(room);
    }

}