package net.sourceforge.fenixedu.applicationTier.Servico.person.parking;

import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import pt.ist.fenixframework.Atomic;

public class AcceptRegulation {

    @Atomic
    public static void run(final ParkingParty parkingParty) {
        parkingParty.setAcceptedRegulation(Boolean.TRUE);
    }
}