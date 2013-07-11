package net.sourceforge.fenixedu.applicationTier.Servico.parking;

import net.sourceforge.fenixedu.domain.parking.ParkingRequestPeriod;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteParkingRequestPeriod {

    @Checked("RolePredicates.PARKING_MANAGER_PREDICATE")
    @Atomic
    public static void run(String id) {
        FenixFramework.<ParkingRequestPeriod> getDomainObject(id).delete();
    }

}