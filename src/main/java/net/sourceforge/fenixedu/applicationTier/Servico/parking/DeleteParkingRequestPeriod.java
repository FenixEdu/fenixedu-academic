package net.sourceforge.fenixedu.applicationTier.Servico.parking;

import net.sourceforge.fenixedu.domain.parking.ParkingRequestPeriod;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DeleteParkingRequestPeriod {

    @Checked("RolePredicates.PARKING_MANAGER_PREDICATE")
    @Service
    public static void run(String id) {
        AbstractDomainObject.<ParkingRequestPeriod> fromExternalId(id).delete();
    }

}