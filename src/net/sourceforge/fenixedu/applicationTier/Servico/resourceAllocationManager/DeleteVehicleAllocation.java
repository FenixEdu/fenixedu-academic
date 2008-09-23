package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.resource.VehicleAllocation;

public class DeleteVehicleAllocation extends FenixService {

    public void run(VehicleAllocation allocation) {
	if (allocation != null) {
	    allocation.delete();
	}
    }
}
