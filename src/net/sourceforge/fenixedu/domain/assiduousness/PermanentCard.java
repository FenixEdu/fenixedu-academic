package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class PermanentCard extends PermanentCard_Base {
    
    public PermanentCard() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
}
