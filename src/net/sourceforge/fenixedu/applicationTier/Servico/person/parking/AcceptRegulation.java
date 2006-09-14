package net.sourceforge.fenixedu.applicationTier.Servico.person.parking;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;

public class AcceptRegulation extends Service {

    public void run(final ParkingParty parkingParty) {
        parkingParty.setAcceptedRegulation(Boolean.TRUE);
    }
}