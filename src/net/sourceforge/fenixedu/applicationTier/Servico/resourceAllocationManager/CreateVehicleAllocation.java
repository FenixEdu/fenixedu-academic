package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.VehicleAllocationBean;
import net.sourceforge.fenixedu.domain.resource.VehicleAllocation;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateVehicleAllocation extends FenixService {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static void run(VehicleAllocationBean bean) {
        if (bean != null) {
            new VehicleAllocation(bean.getBeginDateTime(), bean.getEndDateTime(), bean.getVehicle(), bean.getRequestor(),
                    bean.getReason(), bean.getDistance(), bean.getAmountCharged());
        }
    }
}