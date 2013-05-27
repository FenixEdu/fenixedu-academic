package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import pt.ist.fenixWebFramework.services.Service;

public class LerSala {

    @Service
    public static Object run(RoomKey keySala) {
        final AllocatableSpace sala = AllocatableSpace.findAllocatableSpaceForEducationByName(keySala.getNomeSala());
        return sala == null ? null : InfoRoom.newInfoFromDomain(sala);
    }

}