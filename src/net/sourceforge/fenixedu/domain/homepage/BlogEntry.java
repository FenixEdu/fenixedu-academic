package net.sourceforge.fenixedu.domain.homepage;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class BlogEntry extends BlogEntry_Base {
    
    public BlogEntry() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
}
