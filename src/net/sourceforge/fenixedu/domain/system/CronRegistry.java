package net.sourceforge.fenixedu.domain.system;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class CronRegistry extends CronRegistry_Base {
    
    public CronRegistry() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
}
