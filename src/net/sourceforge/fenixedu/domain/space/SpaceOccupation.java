package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class SpaceOccupation extends SpaceOccupation_Base {
    
    public SpaceOccupation() {
        super();
        setOjbConcreteClass(this.getClass().getName());
        setRootDomainObject(RootDomainObject.getInstance());
    }       
    
    public void delete() {
        removeSpace();
        removeRootDomainObject();
        deleteDomainObject();
    }
}
