package net.sourceforge.fenixedu.domain.cms.website;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class Website extends Website_Base {

    public Website() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        removeTarget();
        removeRootDomainObject();
        super.delete();
    }
    
}
