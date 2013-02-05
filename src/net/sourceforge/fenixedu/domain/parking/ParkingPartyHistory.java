package net.sourceforge.fenixedu.domain.parking;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class ParkingPartyHistory extends ParkingPartyHistory_Base {

    public ParkingPartyHistory(ParkingParty parkingParty, Boolean onlineRequest) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setParty(parkingParty.getParty());
        setCardStartDate(parkingParty.getCardStartDate());
        setCardEndDate(parkingParty.getCardEndDate());
        setCardNumber(parkingParty.getCardNumber());
        setParkingGroup(parkingParty.getParkingGroup());
        setPhdNumber(parkingParty.getPhdNumber());
        setNotes(parkingParty.getNotes());
        setRequestedAs(parkingParty.getRequestedAs());
        setUsedNumber(parkingParty.getUsedNumber());
        setHistoryDate(new DateTime());
        setOnlineRequest(onlineRequest);
    }

}
