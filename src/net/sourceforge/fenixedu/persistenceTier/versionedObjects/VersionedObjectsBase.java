package net.sourceforge.fenixedu.persistenceTier.versionedObjects;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;


public abstract class VersionedObjectsBase {

    public void deleteByOID(Class classToQuery, Integer oid) throws ExcepcaoPersistencia {
        // throw new RuntimeException("This method should not be called by the
        // Versioned Object DAO's!");
    }

    public final DomainObject readByOId(final DomainObject obj, final boolean lockWrite) {
        try {
            return SuportePersistenteOJB.getInstance().getIPersistentObject().readByOId(obj, false);
        } catch (ExcepcaoPersistencia e) {
            return null;
        }
    }

    public final DomainObject readByOID(final Class classToQuery, final Integer oid)
            throws ExcepcaoPersistencia {
        return SuportePersistenteOJB.getInstance().getIPersistentObject().readByOID(classToQuery, oid);
    }

    public final DomainObject readByOID(Class classToQuery, Integer oid, boolean lockWrite)
            throws ExcepcaoPersistencia {
        return readByOID(classToQuery, oid);
    }

    public Collection readAll(final Class classToQuery) {
	QueryByCriteria queryCriteria = new QueryByCriteria(classToQuery, null, false);
	PersistenceBroker pb = SuportePersistenteOJB.getCurrentPersistenceBroker();
        
	return pb.getCollectionByQuery(queryCriteria);
    }

    public Object lockIteratorNextObj(Iterator iterator) throws ExcepcaoPersistencia {
        throw new RuntimeException("This method should not be called by the Versioned Object DAO's!");
    }

    public int count(Class classToQuery, Criteria criteria) {
        throw new RuntimeException("This method should not be called by the Versioned Object DAO's!");
    }

}
