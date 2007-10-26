package net.sourceforge.fenixedu.applicationTier.Servico.parking;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.parking.ParkingCardSearchBean;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;

public class RenewParkingCards extends Service {

    public ParkingCardSearchBean run(ParkingCardSearchBean parkingCardSearchBean) {

	int groupMembersCount = 0;
	for (ParkingParty parkingParty : rootDomainObject.getParkingParties()) {
	    if (parkingParty.hasAnyVehicles()
		    && parkingParty.getParkingGroup() == parkingCardSearchBean.getParkingGroup()) {
		if (parkingParty.getCardEndDate() != null
			&& parkingParty.getCardEndDate().toYearMonthDay().isEqual(
				parkingCardSearchBean.getActualEndDate())
			&& parkingParty.isActiveInHisGroup()) {
//		    parkingParty.setCardEndDate(parkingCardSearchBean.getRenewalEndDate().toDateTime(
//			    parkingParty.getCardEndDate()));
		} else {
		//    parkingCardSearchBean.getNotRenewedParkingParties().add(parkingParty);
		}
		groupMembersCount++;
	    }
	}
	parkingCardSearchBean.setGroupMembersCount(groupMembersCount);	
	return parkingCardSearchBean;
    }
}
