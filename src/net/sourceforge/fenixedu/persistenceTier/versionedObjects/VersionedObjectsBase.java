package net.sourceforge.fenixedu.persistenceTier.versionedObjects;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
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

    public final DomainObject readByOID(final Class classToQuery, final Integer oid)
            throws ExcepcaoPersistencia {
        return SuportePersistenteOJB.getInstance().getIPersistentObject().readByOID(classToQuery, oid);
    }

    public RootDomainObject readRootDomainObject() throws ExcepcaoPersistencia {
    	return SuportePersistenteOJB.getInstance().getIPersistentObject().readRootDomainObject();
    }

    public Collection readAll(final Class classToQuery) {
	QueryByCriteria queryCriteria = new QueryByCriteria(classToQuery, null, false);
	PersistenceBroker pb = SuportePersistenteOJB.getCurrentPersistenceBroker();
        
	return pb.getCollectionByQuery(queryCriteria);
    }

    public int count(Class classToQuery, Criteria criteria) {
        throw new RuntimeException("This method should not be called by the Versioned Object DAO's!");
    }

}
