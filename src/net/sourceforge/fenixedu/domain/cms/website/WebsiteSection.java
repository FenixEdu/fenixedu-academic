package net.sourceforge.fenixedu.domain.cms.website;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class WebsiteSection extends WebsiteSection_Base {
    
    public WebsiteSection() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

}
