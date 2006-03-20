package net.sourceforge.fenixedu.applicationTier;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;


public class Service implements pt.utl.ist.berserk.logic.serviceManager.IService {

	protected static final RootDomainObject rootDomainObject = RootDomainObject.getInstance();

	@Deprecated
	protected static final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

	@Deprecated
    protected static final IPersistentObject persistentObject = persistentSupport.getIPersistentObject();

}
