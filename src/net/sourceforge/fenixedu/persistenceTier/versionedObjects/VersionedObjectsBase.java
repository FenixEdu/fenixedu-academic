package net.sourceforge.fenixedu.persistenceTier.versionedObjects;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

import org.apache.ojb.broker.query.Criteria;

public abstract class VersionedObjectsBase {

    public void deleteByOID(Class classToQuery, Integer oid) throws ExcepcaoPersistencia {
        //throw new RuntimeException("This method should not be called by the Versioned Object DAO's!");
    }

    public void lockWrite(Object obj) throws ExcepcaoPersistencia {
        throw new RuntimeException("This method should not be called by the Versioned Object DAO's!");
    }

    public final IDomainObject readByOId(final IDomainObject obj, final boolean lockWrite) {
        try {
            return SuportePersistenteOJB.getInstance().getIPersistentObject().readByOId(obj, false);
        } catch (ExcepcaoPersistencia e) {
            return null;
        }
    }

    public void simpleLockWrite(IDomainObject obj) {
        try {
            SuportePersistenteOJB.getInstance().getIPersistentObject().simpleLockWrite(obj);
        } catch (ExcepcaoPersistencia e) {
            throw new RuntimeException(e);
        }
    }

    public final IDomainObject readByOID(final Class classToQuery, final Integer oid) throws ExcepcaoPersistencia {
        return SuportePersistenteOJB.getInstance().getIPersistentObject().readByOID(classToQuery, oid);
    }

    public final IDomainObject readByOID(Class classToQuery, Integer oid, boolean lockWrite) throws ExcepcaoPersistencia {
        return readByOID(classToQuery, oid);
    }
    
    protected Collection readAll(final Class classToQuery) throws ExcepcaoPersistencia {
        return null;
    }

    public Object lockIteratorNextObj(Iterator iterator) throws ExcepcaoPersistencia {
        throw new RuntimeException("This method should not be called by the Versioned Object DAO's!");
    }

    public int count(Class classToQuery, Criteria criteria) {
        throw new RuntimeException("This method should not be called by the Versioned Object DAO's!");
    }

    public IDomainObject materialize(IDomainObject domainObject) {
        throw new RuntimeException("This method should not be called by the Versioned Object DAO's!");
    }

}
