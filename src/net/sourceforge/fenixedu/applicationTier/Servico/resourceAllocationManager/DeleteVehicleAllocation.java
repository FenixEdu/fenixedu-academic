package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.resource.VehicleAllocation;

public class DeleteVehicleAllocation extends Service {

    public void run(VehicleAllocation allocation) {
	if (allocation != null) {
	    allocation.delete();
	}
    }
}
