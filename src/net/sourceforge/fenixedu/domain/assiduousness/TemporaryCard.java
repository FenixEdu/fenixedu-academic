package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class TemporaryCard extends TemporaryCard_Base {
    
    public TemporaryCard() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
}
