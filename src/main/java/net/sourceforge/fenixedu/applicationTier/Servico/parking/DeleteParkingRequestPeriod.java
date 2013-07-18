package net.sourceforge.fenixedu.applicationTier.Servico.parking;


import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteParkingRequestPeriod {

    @Checked("RolePredicates.PARKING_MANAGER_PREDICATE")
    @Service
    public static void run(Integer id) {
        RootDomainObject.getInstance().readParkingRequestPeriodByOID(id).delete();
    }

}