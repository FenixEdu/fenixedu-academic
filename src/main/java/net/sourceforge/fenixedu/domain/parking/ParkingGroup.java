package net.sourceforge.fenixedu.domain.parking;

import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ParkingGroup extends ParkingGroup_Base {

    public ParkingGroup(String groupName) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setGroupName(groupName);
    }

    public static List<ParkingGroup> getAll() {
        return RootDomainObject.getInstance().getParkingGroups();
    }
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.parking.ParkingParty> getParkingParties() {
        return getParkingPartiesSet();
    }

    @Deprecated
    public boolean hasAnyParkingParties() {
        return !getParkingPartiesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.parking.ParkingPartyHistory> getParkingPartyHistories() {
        return getParkingPartyHistoriesSet();
    }

    @Deprecated
    public boolean hasAnyParkingPartyHistories() {
        return !getParkingPartyHistoriesSet().isEmpty();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasGroupName() {
        return getGroupName() != null;
    }

    @Deprecated
    public boolean hasGroupDescription() {
        return getGroupDescription() != null;
    }

}
