package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.Filtro.spaceManager.SpaceAdministratorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import pt.ist.fenixframework.Atomic;

public class MergeRooms {

    protected void run(AllocatableSpace fromRoom, AllocatableSpace destinationRoom) {
        AllocatableSpace.mergeAllocatableSpaces(fromRoom, destinationRoom);
    }

    // Service Invokers migrated from Berserk

    private static final MergeRooms serviceInstance = new MergeRooms();

    @Atomic
    public static void runMergeRooms(AllocatableSpace fromRoom, AllocatableSpace destinationRoom) throws NotAuthorizedException {
        SpaceAdministratorAuthorizationFilter.instance.execute(fromRoom, destinationRoom);
        serviceInstance.run(fromRoom, destinationRoom);
    }

}