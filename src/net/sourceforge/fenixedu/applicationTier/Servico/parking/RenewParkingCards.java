package net.sourceforge.fenixedu.applicationTier.Servico.parking;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.parking.ParkingGroup;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;

import org.joda.time.DateTime;

public class RenewParkingCards extends FenixService {

    @Checked("RolePredicates.PARKING_MANAGER_PREDICATE")
    @Service
    public static void run(List<ParkingParty> parkingParties, DateTime newEndDate, ParkingGroup newParkingGroup) {
	DateTime newBeginDate = new DateTime();
	for (ParkingParty parkingParty : parkingParties) {
	    parkingParty.renewParkingCard(newBeginDate, newEndDate, newParkingGroup);
	}
    }
}