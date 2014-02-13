package pt.ist.fenix.applicationTier.Servico.person.parking;

import pt.ist.fenix.domain.parking.ParkingRequest;
import pt.ist.fenixframework.Atomic;

public class RenewUnlimitedParkingRequest {

    @Atomic
    public static void run(ParkingRequest oldParkingRequest, Boolean limitlessAccessCard) {
        if (oldParkingRequest.getParkingParty().getCanRequestUnlimitedCardAndIsInAnyRequestPeriod()) {
            new ParkingRequest(oldParkingRequest, limitlessAccessCard);
        }
    }
}