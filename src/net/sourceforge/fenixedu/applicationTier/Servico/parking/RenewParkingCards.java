package net.sourceforge.fenixedu.applicationTier.Servico.parking;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.parking.ParkingGroup;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;

import org.joda.time.DateTime;

public class RenewParkingCards extends FenixService {

    public void run(List<ParkingParty> parkingParties, DateTime newEndDate, ParkingGroup newParkingGroup) {
	DateTime newBeginDate = new DateTime();
	for (ParkingParty parkingParty : parkingParties) {
	    parkingParty.renewParkingCard(newBeginDate, newEndDate, newParkingGroup);
	}
    }
}
