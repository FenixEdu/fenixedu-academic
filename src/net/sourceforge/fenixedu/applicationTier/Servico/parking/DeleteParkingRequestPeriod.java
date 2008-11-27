package net.sourceforge.fenixedu.applicationTier.Servico.parking;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;

public class DeleteParkingRequestPeriod extends FenixService {

    @Checked("RolePredicates.PARKING_MANAGER_PREDICATE")
    @Service
    public static void run(Integer id) {
	rootDomainObject.readParkingRequestPeriodByOID(id).delete();
    }

}