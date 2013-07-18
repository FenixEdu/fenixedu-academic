package net.sourceforge.fenixedu.applicationTier.Servico.resourceManager;


import net.sourceforge.fenixedu.domain.resource.Vehicle;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteVehicle {

    @Checked("RolePredicates.RESOURCE_MANAGER_PREDICATE")
    @Service
    public static void run(Vehicle vehicle) {
        if (vehicle != null) {
            vehicle.delete();
        }
    }
}