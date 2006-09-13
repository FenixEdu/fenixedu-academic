package net.sourceforge.fenixedu.applicationTier.Servico.person.parking;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;

public class CreateParkingParty extends Service {

    public ParkingParty run(final Party party) {
        return new ParkingParty(party);
    }
}