package pt.ist.fenix.applicationTier.Servico.person.parking;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import pt.ist.fenix.domain.parking.ParkingParty;
import pt.ist.fenixframework.Atomic;

public class CreateParkingParty {

    @Atomic
    public static ParkingParty run(final Party party) {
        return new ParkingParty(party);
    }
}