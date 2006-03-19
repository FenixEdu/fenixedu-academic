package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class Leave extends Leave_Base {
    
    public Leave() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
}
