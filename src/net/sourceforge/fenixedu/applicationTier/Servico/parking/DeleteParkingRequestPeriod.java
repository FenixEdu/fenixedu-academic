package net.sourceforge.fenixedu.applicationTier.Servico.parking;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteParkingRequestPeriod extends FenixService {

	@Checked("RolePredicates.PARKING_MANAGER_PREDICATE")
	@Service
	public static void run(Integer id) {
		rootDomainObject.readParkingRequestPeriodByOID(id).delete();
	}

}