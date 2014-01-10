package net.sourceforge.fenixedu.applicationTier.Servico.person.parking;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import pt.ist.fenixframework.Atomic;

public class CreateParkingParty {

    @Atomic
    public static ParkingParty run(final Party party) {
        return new ParkingParty(party);
    }
}