package net.sourceforge.fenixedu.applicationTier.Servico.parking;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.parking.ParkingRequestPeriod;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteParkingRequestPeriod {

    @Atomic
    public static void run(String id) {
        check(RolePredicates.PARKING_MANAGER_PREDICATE);
        FenixFramework.<ParkingRequestPeriod> getDomainObject(id).delete();
    }

}