package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class Holiday extends Holiday_Base {
    
    public  Holiday() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
}
