package net.sourceforge.fenixedu.applicationTier.Servico.parking;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;

import org.joda.time.DateTime;

public class RenewParkingCards extends Service {

    public void run(List<ParkingParty> parkingParties, DateTime newEndDate) {

	for (ParkingParty parkingParty : parkingParties) {
	    parkingParty.setCardEndDate(newEndDate);
	}	
    }
}
