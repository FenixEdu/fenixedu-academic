package net.sourceforge.fenixedu.applicationTier.Servico.resourceManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.resource.Vehicle;

public class DeleteVehicle extends FenixService {

    public void run(Vehicle vehicle) {
	if (vehicle != null) {
	    vehicle.delete();
	}
    }
}
