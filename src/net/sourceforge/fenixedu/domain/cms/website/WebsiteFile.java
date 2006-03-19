package net.sourceforge.fenixedu.domain.cms.website;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class WebsiteFile extends WebsiteFile_Base {
    
    public WebsiteFile() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
}
