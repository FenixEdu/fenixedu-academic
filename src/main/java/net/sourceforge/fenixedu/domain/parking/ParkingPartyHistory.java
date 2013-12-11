package net.sourceforge.fenixedu.domain.parking;

import pt.ist.bennu.core.domain.Bennu;

import org.joda.time.DateTime;

public class ParkingPartyHistory extends ParkingPartyHistory_Base {

    public ParkingPartyHistory(ParkingParty parkingParty, Boolean onlineRequest) {
        super();
        setRootDomainObject(Bennu.getInstance());
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

    @Deprecated
    public boolean hasNotes() {
        return getNotes() != null;
    }

    @Deprecated
    public boolean hasCardNumber() {
        return getCardNumber() != null;
    }

    @Deprecated
    public boolean hasOnlineRequest() {
        return getOnlineRequest() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasRequestedAs() {
        return getRequestedAs() != null;
    }

    @Deprecated
    public boolean hasPhdNumber() {
        return getPhdNumber() != null;
    }

    @Deprecated
    public boolean hasHistoryDate() {
        return getHistoryDate() != null;
    }

    @Deprecated
    public boolean hasParty() {
        return getParty() != null;
    }

    @Deprecated
    public boolean hasCardStartDate() {
        return getCardStartDate() != null;
    }

    @Deprecated
    public boolean hasUsedNumber() {
        return getUsedNumber() != null;
    }

    @Deprecated
    public boolean hasParkingGroup() {
        return getParkingGroup() != null;
    }

    @Deprecated
    public boolean hasCardEndDate() {
        return getCardEndDate() != null;
    }

}
