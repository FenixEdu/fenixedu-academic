package org.fenixedu.parking.applicationTier.Servico.person.parking;

import org.fenixedu.parking.domain.ParkingParty;

import pt.ist.fenixframework.Atomic;

public class AcceptRegulation {

    @Atomic
    public static void run(final ParkingParty parkingParty) {
        parkingParty.setAcceptedRegulation(Boolean.TRUE);
    }
}