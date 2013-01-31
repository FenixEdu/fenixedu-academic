package net.sourceforge.fenixedu.applicationTier;

import net.sourceforge.fenixedu.domain.RootDomainObject;

@Deprecated
public class FenixService implements pt.utl.ist.berserk.logic.serviceManager.IService {

	protected static RootDomainObject rootDomainObject;

	public static void init(RootDomainObject instance) {
		FenixService.rootDomainObject = instance;
	}

}
