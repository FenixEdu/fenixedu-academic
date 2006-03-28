package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class PersonalCard extends PersonalCard_Base {
    
    public  PersonalCard() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
}
