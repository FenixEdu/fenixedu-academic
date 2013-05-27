package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import pt.ist.fenixWebFramework.services.Service;

public class ReadRoomByOID {

    @Service
    public static InfoRoom run(Integer oid) {
        final AllocatableSpace room = (AllocatableSpace) RootDomainObject.getInstance().readResourceByOID(oid);
        return InfoRoom.newInfoFromDomain(room);
    }

}