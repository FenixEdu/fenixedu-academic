package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomEditor;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class EditarSala {

    @Atomic
    public static void run(InfoRoomEditor salaNova) throws ExistingServiceException {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        if (salaNova != null) {
            AllocatableSpace room = salaNova.getRoom();
            if (room != null) {
                room.editCapacities(salaNova.getCapacidadeNormal(), salaNova.getCapacidadeExame());
            }
        }
    }
}