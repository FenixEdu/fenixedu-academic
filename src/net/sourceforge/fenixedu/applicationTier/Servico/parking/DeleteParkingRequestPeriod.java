package net.sourceforge.fenixedu.applicationTier.Servico.parking;

import net.sourceforge.fenixedu.applicationTier.FenixService;

public class DeleteParkingRequestPeriod extends FenixService {

    public void run(Integer id) {
	rootDomainObject.readParkingRequestPeriodByOID(id).delete();
    }

}
