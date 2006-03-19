package net.sourceforge.fenixedu.domain.cms.website;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class WebsiteItem extends WebsiteItem_Base {
    
    public WebsiteItem() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
}
