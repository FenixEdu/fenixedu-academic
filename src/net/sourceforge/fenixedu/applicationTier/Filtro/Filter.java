package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.filterManager.IFilter;

abstract public class Filter implements IFilter {

    protected static final RootDomainObject rootDomainObject = RootDomainObject.getInstance();
    
    @Deprecated
	protected static final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

    @Deprecated
    protected static final IPersistentObject persistentObject = persistentSupport.getIPersistentObject();
    
}
