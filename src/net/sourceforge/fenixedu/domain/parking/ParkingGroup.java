package net.sourceforge.fenixedu.domain.parking;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ParkingGroup extends ParkingGroup_Base {
    
    public  ParkingGroup() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
}
