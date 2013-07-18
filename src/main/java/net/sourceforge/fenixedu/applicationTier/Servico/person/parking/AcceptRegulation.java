package net.sourceforge.fenixedu.applicationTier.Servico.person.parking;


import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import pt.ist.fenixWebFramework.services.Service;

public class AcceptRegulation {

    @Service
    public static void run(final ParkingParty parkingParty) {
        parkingParty.setAcceptedRegulation(Boolean.TRUE);
    }
}