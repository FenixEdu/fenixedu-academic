package net.sourceforge.fenixedu.applicationTier.Servico.person.parking;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest;

public class RenewUnlimitedParkingRequest extends Service {

    public void run(ParkingRequest oldParkingRequest, Boolean limitlessAccessCard) {
	new ParkingRequest(oldParkingRequest, limitlessAccessCard);
    }
}