package org.fenixedu.parking.applicationTier.Servico.person.parking;

import org.fenixedu.parking.domain.ParkingParty;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import pt.ist.fenixframework.Atomic;

public class CreateParkingParty {

    @Atomic
    public static ParkingParty run(final Party party) {
        return new ParkingParty(party);
    }
}