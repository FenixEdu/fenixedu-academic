package pt.ist.fenix.applicationTier.Servico.person.parking;

import pt.ist.fenix.domain.parking.ParkingParty;
import pt.ist.fenixframework.Atomic;

public class AcceptRegulation {

    @Atomic
    public static void run(final ParkingParty parkingParty) {
        parkingParty.setAcceptedRegulation(Boolean.TRUE);
    }
}