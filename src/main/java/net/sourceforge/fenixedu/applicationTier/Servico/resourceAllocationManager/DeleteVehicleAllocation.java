package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.resource.VehicleAllocation;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class DeleteVehicleAllocation {

    @Atomic
    public static void run(VehicleAllocation allocation) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        if (allocation != null) {
            allocation.delete();
        }
    }
}