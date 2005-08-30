package net.sourceforge.fenixedu.stm;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerException;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.OptimisticLockException;
import org.apache.ojb.broker.accesslayer.LookupException;

import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.CollectionDescriptor;
import org.apache.ojb.broker.core.ValueContainer;

import org.apache.ojb.broker.util.ObjectModificationDefaultImpl;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.cache.FenixCache;


class DBChanges {
    private PersistenceBroker broker;
    private Set<AttrChangeLog> attrChangeLogs = null;
    private Set<DomainObject> newObjs = null;
    private Set objsToStore = null;
    private Set objsToDelete = null;
    private Map<RelationTupleInfo,RelationTupleInfo> mToNTuples = null;

    public boolean needsWrite() {
	return ((newObjs != null) && (! newObjs.isEmpty()))
	    || ((objsToStore != null) && (! objsToStore.isEmpty()))
	    || ((objsToDelete != null) && (! objsToDelete.isEmpty()))
	    || ((mToNTuples != null) && (! mToNTuples.isEmpty()));
    }

    void finish() {
	if (broker != null) {
	    broker.close();
	    broker = null;
	}
    }


    public PersistenceBroker getOJBBroker() {
	if (broker == null) {
	    broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	}
	return broker;
    }

    public void logAttrChange(DomainObject obj, String attrName) {
	if (attrChangeLogs == null) {
	    attrChangeLogs = new HashSet<AttrChangeLog>();
	}
	attrChangeLogs.add(new AttrChangeLog(obj, attrName));
    }

    public void storeNewObject(DomainObject obj) {
	if (newObjs == null) {
	    newObjs = new HashSet<DomainObject>();
	}
	newObjs.add(obj);
	if (objsToDelete != null) {
	    objsToDelete.remove(obj);
	}
    }

    public void storeObject(DomainObject obj, String attrName) {
	logAttrChange(obj, attrName);

	if (objsToStore == null) {
	    objsToStore = new HashSet();
	}
	objsToStore.add(obj);
	if (objsToDelete != null) {
	    objsToDelete.remove(obj);
	}
    }

    public void deleteObject(Object obj) {
	if (objsToDelete == null) {
	    objsToDelete = new HashSet();
	}
	objsToDelete.add(obj);
	if (newObjs != null) {
	    newObjs.remove(obj);
	}
	if (objsToStore != null) {
	    objsToStore.remove(obj);
	}
    }

    public void addRelationTuple(String relation, Object obj1, String colNameOnObj1, Object obj2, String colNameOnObj2) {
	setRelationTuple(relation, obj1, colNameOnObj1, obj2, colNameOnObj2, false);
    }

    public void removeRelationTuple(String relation, Object obj1, String colNameOnObj1, Object obj2, String colNameOnObj2) {
	setRelationTuple(relation, obj1, colNameOnObj1, obj2, colNameOnObj2, true);
    }

    private void setRelationTuple(String relation, Object obj1, String colNameOnObj1, Object obj2, String colNameOnObj2, boolean remove) {
	if (mToNTuples == null) {
	    mToNTuples = new HashMap<RelationTupleInfo,RelationTupleInfo>();
	}

	RelationTupleInfo info = new RelationTupleInfo(relation, obj1, colNameOnObj1, obj2, colNameOnObj2, remove);
	RelationTupleInfo previous = mToNTuples.get(info);

	if (previous == null) {
	    mToNTuples.put(info, info);
	} else {
	    if (previous.remove != remove) {
		mToNTuples.remove(previous);
	    }
	}
    }

    void cache() {
	FenixCache cache = Transaction.getCache();

	if (newObjs != null) {
	    for (DomainObject obj : newObjs) {
		cache.cache(obj);
	    }
	}
    }

    void makePersistent(int txNumber) throws SQLException, LookupException {
	PersistenceBroker pb = getOJBBroker();

	// store new objects
	if (newObjs != null) {
	    for (Object obj : newObjs) {
		pb.store(obj, ObjectModificationDefaultImpl.INSERT);
	    }
	}
	
	boolean foundOptimisticException = false;
	
	// update objects
	if (objsToStore != null) {
	    for (Object obj : objsToStore) {
		try {
		    pb.store(obj, ObjectModificationDefaultImpl.UPDATE);
		} catch (OptimisticLockException ole) {
		    pb.removeFromCache(obj);
		    foundOptimisticException = true;
		}
	    }
	}
	
	if (foundOptimisticException) {
	    throw new jvstm.CommitException();
	}

	// delete objects
	if (objsToDelete != null) {
	    for (Object obj : objsToDelete) {
		pb.delete(obj);
	    }
	}
	
	// write m-to-n tuples
	if (mToNTuples != null) {
	    for (RelationTupleInfo info : mToNTuples.values()) {
		updateMtoNRelation(pb, info);
	    }		
	}
	    
	// write change logs
	Connection conn = pb.serviceConnectionManager().getConnection();
	if (attrChangeLogs != null) {
	    PreparedStatement stmt = conn.prepareStatement("INSERT INTO TX_CHANGE_LOGS VALUES (?,?,?,?)");

	    stmt.setInt(4, txNumber);

	    for (AttrChangeLog log : attrChangeLogs) {
		stmt.setString(1, log.obj.getClass().getName());
		stmt.setInt(2, log.obj.getIdInternal());
		stmt.setString(3, log.attr);

		stmt.executeUpdate();
	    }
	}

	// write ServiceInfo
	ServiceInfo info = ServiceInfo.getCurrentServiceInfo();
	if ((info != null) && info.shouldLog()) {
	    PreparedStatement stmt = conn.prepareStatement("INSERT INTO SERVICE_LOG VALUES (?,?,?,?)");
	    stmt.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
	    stmt.setString(2, info.username);
	    stmt.setString(3, info.serviceName);
	    stmt.setString(4, info.getArgumentsAsString());
	    stmt.executeUpdate();
	}
    }


    // copied and adapted from OJB's MtoNBroker
    protected void updateMtoNRelation(PersistenceBroker pb, RelationTupleInfo tupleInfo) {
	Object obj1 = tupleInfo.obj1;
	Object obj2 = tupleInfo.obj2;

        ClassDescriptor cld1 = pb.getDescriptorRepository().getDescriptorFor(obj1.getClass());
	CollectionDescriptor cod = cld1.getCollectionDescriptorByName(tupleInfo.colNameOnObj1);
	if (cod == null) {
	    // try the mapping on the other object
	    cld1 = pb.getDescriptorRepository().getDescriptorFor(obj2.getClass());
	    cod = cld1.getCollectionDescriptorByName(tupleInfo.colNameOnObj2);

	    // switch objects
	    obj1 = tupleInfo.obj2;
	    obj2 = tupleInfo.obj1;
	}

        ValueContainer[] pkValues1 = pb.serviceBrokerHelper().getKeyValues(cld1, obj1);
        String[] pkColumns1 = cod.getFksToThisClass();
	
        ClassDescriptor cld2 = pb.getDescriptorRepository().getDescriptorFor(obj2.getClass());
        ValueContainer[] pkValues2 = pb.serviceBrokerHelper().getKeyValues(cld2, obj2);	
        String[] pkColumns2 = cod.getFksToItemClass();

        String table = cod.getIndirectionTable();
	
	String sqlStmt = null;
	if (tupleInfo.remove) {
	    sqlStmt = pb.serviceSqlGenerator().getDeleteMNStatement(table, pkColumns1, pkColumns2);
	} else {
	    sqlStmt = pb.serviceSqlGenerator().getInsertMNStatement(table, pkColumns1, pkColumns2);
	}

        pb.serviceJdbcAccess().executeUpdateSQL(sqlStmt, cld1, pkValues1, pkValues2);
    }

    static class RelationTupleInfo {
	final String relation;
	final Object obj1;
	final String colNameOnObj1;
	final Object obj2;
	final String colNameOnObj2;
	final boolean remove;

	RelationTupleInfo(String relation, Object obj1, String colNameOnObj1, Object obj2, String colNameOnObj2, boolean remove) {
	    this.relation = relation;
	    this.obj1 = obj1;
	    this.colNameOnObj1 = colNameOnObj1;
	    this.obj2 = obj2;
	    this.colNameOnObj2 = colNameOnObj2;
	    this.remove = remove;
	}

	public int hashCode() {
	    return relation.hashCode() + obj1.hashCode() + obj2.hashCode();
	}
	
	public boolean equals(Object obj) {
	    if ((obj != null) && (obj.getClass() == this.getClass())) {
		RelationTupleInfo other = (RelationTupleInfo)obj;
		return this.relation.equals(other.relation) && this.obj1.equals(other.obj1) && this.obj2.equals(other.obj2);
	    } else {
		return false;
	    }
	}
    }

    static class AttrChangeLog {
	final DomainObject obj;
	final String attr;

	AttrChangeLog(DomainObject obj, String attr) {
	    this.obj = obj;
	    this.attr = attr;
	}

	public int hashCode() {
	    return System.identityHashCode(obj) + attr.hashCode();
	}
	
	public boolean equals(Object obj) {
	    if ((obj != null) && (obj.getClass() == this.getClass())) {
		AttrChangeLog other = (AttrChangeLog)obj;
		return (this.obj == other.obj) && this.attr.equals(other.attr);
	    } else {
		return false;
	    }
	}
    }
}
