package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import net.sourceforge.fenixedu.domain.resource.VehicleAllocation;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteVehicleAllocation {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static void run(VehicleAllocation allocation) {
        if (allocation != null) {
            allocation.delete();
        }
    }
}