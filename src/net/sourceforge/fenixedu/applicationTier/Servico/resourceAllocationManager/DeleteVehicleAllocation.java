package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.resource.VehicleAllocation;

public class DeleteVehicleAllocation extends FenixService {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static void run(VehicleAllocation allocation) {
	if (allocation != null) {
	    allocation.delete();
	}
    }
}