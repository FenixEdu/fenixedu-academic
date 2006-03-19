package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class Schedule extends Schedule_Base {
    
    public Schedule() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
}
