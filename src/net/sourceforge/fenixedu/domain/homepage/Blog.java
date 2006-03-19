package net.sourceforge.fenixedu.domain.homepage;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class Blog extends Blog_Base {
    
    public Blog() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
}
