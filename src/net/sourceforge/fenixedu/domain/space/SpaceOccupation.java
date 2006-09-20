package net.sourceforge.fenixedu.domain.space;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;

public abstract class SpaceOccupation extends SpaceOccupation_Base {
    
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
    
    public abstract Group getAccessGroup();
}
