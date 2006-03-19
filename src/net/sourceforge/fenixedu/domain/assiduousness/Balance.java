package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class Balance extends Balance_Base{

    public Balance() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

}
