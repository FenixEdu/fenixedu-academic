/*
 * Created on 2005/03/27
 * 
 */
package net.sourceforge.fenixedu.persistenceTier;

/**
 * 
 * @author Luis Cruz
 */
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.delegatedObjects.DelegatePersistenceSupport;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsPersistenceSupport;

public class PersistenceSupportFactory {

    public static ISuportePersistente getDefaultPersistenceSupport() throws ExcepcaoPersistencia {
        return getDelegatePersistenceSupport();
    }

    public static SuportePersistenteOJB getOJBPersistenceSupport() throws ExcepcaoPersistencia {
        return SuportePersistenteOJB.getInstance();
    }

    public static VersionedObjectsPersistenceSupport getVersionedObjectsPersistenceSupport()
            throws ExcepcaoPersistencia {
        return VersionedObjectsPersistenceSupport.getInstance();
    }

    public static DelegatePersistenceSupport getDelegatePersistenceSupport()
            throws ExcepcaoPersistencia {
        return DelegatePersistenceSupport.getInstance();
    }

}