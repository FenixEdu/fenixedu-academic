package org.fenixedu.parking.applicationTier.Servico.parking;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import org.fenixedu.parking.domain.ParkingRequestPeriod;

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