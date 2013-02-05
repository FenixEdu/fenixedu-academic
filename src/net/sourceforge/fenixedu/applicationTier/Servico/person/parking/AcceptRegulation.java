package net.sourceforge.fenixedu.applicationTier.Servico.person.parking;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import pt.ist.fenixWebFramework.services.Service;

public class AcceptRegulation extends FenixService {

    @Service
    public static void run(final ParkingParty parkingParty) {
        parkingParty.setAcceptedRegulation(Boolean.TRUE);
    }
}