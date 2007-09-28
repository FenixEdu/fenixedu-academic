package net.sourceforge.fenixedu.applicationTier.Servico.parking;

import net.sourceforge.fenixedu.applicationTier.Service;

public class DeleteParkingRequestPeriod extends Service {

    public void run(Integer id) {
	rootDomainObject.readParkingRequestPeriodByOID(id).delete();
    }

}
