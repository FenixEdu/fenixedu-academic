package net.sourceforge.fenixedu.applicationTier;

import net.sourceforge.fenixedu.domain.RootDomainObject;


public class Service implements pt.utl.ist.berserk.logic.serviceManager.IService {

    protected static RootDomainObject rootDomainObject;

    public static void init(RootDomainObject instance) {
        Service.rootDomainObject = instance;
    }
    
}
