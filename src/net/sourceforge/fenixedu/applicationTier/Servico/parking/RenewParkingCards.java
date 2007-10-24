package net.sourceforge.fenixedu.applicationTier.Servico.parking;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.parking.ParkingCardsRenewalBean;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;

public class RenewParkingCards extends Service {

    public ParkingCardsRenewalBean run(ParkingCardsRenewalBean parkingCardsRenewalBean) {

	int groupMembersCount = 0;
	for (ParkingParty parkingParty : rootDomainObject.getParkingParties()) {
	    if (parkingParty.hasAnyVehicles()
		    && parkingParty.getParkingGroup() == parkingCardsRenewalBean.getParkingGroup()) {
		if (parkingParty.getCardEndDate() != null
			&& parkingParty.getCardEndDate().toYearMonthDay().isEqual(
				parkingCardsRenewalBean.getActualEndDate())
			&& parkingParty.isActiveInHisGroup()) {
//		    parkingParty.setCardEndDate(parkingCardsRenewalBean.getRenewalEndDate().toDateTime(
//			    parkingParty.getCardEndDate()));
		} else {
		    parkingCardsRenewalBean.getNotRenewedParkingParties().add(parkingParty);
		}
		groupMembersCount++;
	    }
	}
	parkingCardsRenewalBean.setGroupMembersCount(groupMembersCount);
	parkingCardsRenewalBean.setProcessed(Boolean.TRUE);
	return parkingCardsRenewalBean;
    }
}
