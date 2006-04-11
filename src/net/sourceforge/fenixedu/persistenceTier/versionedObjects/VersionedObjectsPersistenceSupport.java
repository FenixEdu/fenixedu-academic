package net.sourceforge.fenixedu.persistenceTier.versionedObjects;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantContractVO;

public class VersionedObjectsPersistenceSupport implements ISuportePersistente {

    private static final VersionedObjectsPersistenceSupport instance = new VersionedObjectsPersistenceSupport();

    static {
    }

    private VersionedObjectsPersistenceSupport() {
    }

    public static VersionedObjectsPersistenceSupport getInstance() {
        return instance;
    }

    public void confirmarTransaccao() throws ExcepcaoPersistencia {
        return;
    }

    public void iniciarTransaccao() throws ExcepcaoPersistencia {
        return;
    }

    public IPersistentGrantContract getIPersistentGrantContract() {
        return new GrantContractVO();
    }

    public Integer getNumberCachedItems() {
        return null;
    }

    public void cancelarTransaccao() throws ExcepcaoPersistencia {
        return;
    }
    
    public void clearCache() {
        return;
    }

    public IPersistentObject getIPersistentObject() {
        return null;
    }	    
}
