package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomEditor;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class EditarSala {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Atomic
    public static void run(InfoRoomEditor salaNova) throws ExistingServiceException {
        if (salaNova != null) {
            AllocatableSpace room = salaNova.getRoom();
            if (room != null) {
                room.editCapacities(salaNova.getCapacidadeNormal(), salaNova.getCapacidadeExame());
            }
        }
    }
}