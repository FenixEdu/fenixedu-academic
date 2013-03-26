package net.sourceforge.fenixedu.applicationTier.Servico.person.parking;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest;
import pt.ist.fenixWebFramework.services.Service;

public class RenewUnlimitedParkingRequest extends FenixService {

    @Service
    public static void run(ParkingRequest oldParkingRequest, Boolean limitlessAccessCard) {
        if (oldParkingRequest.getParkingParty().getCanRequestUnlimitedCardAndIsInAnyRequestPeriod()) {
            new ParkingRequest(oldParkingRequest, limitlessAccessCard);
        }
    }
}