package net.sourceforge.fenixedu.applicationTier.Servico.person.parking;

import net.sourceforge.fenixedu.domain.parking.ParkingRequest;
import pt.ist.fenixframework.Atomic;

public class RenewUnlimitedParkingRequest {

    @Atomic
    public static void run(ParkingRequest oldParkingRequest, Boolean limitlessAccessCard) {
        if (oldParkingRequest.getParkingParty().getCanRequestUnlimitedCardAndIsInAnyRequestPeriod()) {
            new ParkingRequest(oldParkingRequest, limitlessAccessCard);
        }
    }
}