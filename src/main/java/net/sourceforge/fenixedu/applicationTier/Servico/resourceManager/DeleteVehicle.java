package net.sourceforge.fenixedu.applicationTier.Servico.resourceManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.resource.Vehicle;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class DeleteVehicle {

    @Atomic
    public static void run(Vehicle vehicle) {
        check(RolePredicates.RESOURCE_MANAGER_PREDICATE);
        if (vehicle != null) {
            vehicle.delete();
        }
    }
}