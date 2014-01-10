package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.VehicleAllocationBean;
import net.sourceforge.fenixedu.domain.resource.VehicleAllocation;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class CreateVehicleAllocation {

    @Atomic
    public static void run(VehicleAllocationBean bean) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        if (bean != null) {
            new VehicleAllocation(bean.getBeginDateTime(), bean.getEndDateTime(), bean.getVehicle(), bean.getRequestor(),
                    bean.getReason(), bean.getDistance(), bean.getAmountCharged());
        }
    }
}