package org.fenixedu.parking.domain;

import java.util.Collection;

import org.fenixedu.bennu.core.domain.Bennu;

public class ParkingGroup extends ParkingGroup_Base {

    public ParkingGroup(String groupName) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setGroupName(groupName);
    }

    public static Collection<ParkingGroup> getAll() {
        return Bennu.getInstance().getParkingGroupsSet();
    }

    @Deprecated
    public java.util.Set<ParkingParty> getParkingParties() {
        return getParkingPartiesSet();
    }

    @Deprecated
    public boolean hasAnyParkingParties() {
        return !getParkingPartiesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<ParkingPartyHistory> getParkingPartyHistories() {
        return getParkingPartyHistoriesSet();
    }

    @Deprecated
    public boolean hasAnyParkingPartyHistories() {
        return !getParkingPartyHistoriesSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
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
