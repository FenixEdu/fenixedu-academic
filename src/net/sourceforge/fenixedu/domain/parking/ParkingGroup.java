package net.sourceforge.fenixedu.domain.parking;

import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ParkingGroup extends ParkingGroup_Base {

    public ParkingGroup() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public static List<ParkingGroup> getAll() {
        return RootDomainObject.getInstance().getParkingGroups();
    }
}
