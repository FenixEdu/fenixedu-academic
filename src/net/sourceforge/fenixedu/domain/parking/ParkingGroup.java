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
}
