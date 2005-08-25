package net.sourceforge.fenixedu.stm;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import java.sql.Connection;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerException;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.CollectionDescriptor;
import org.apache.ojb.broker.core.ValueContainer;

import org.apache.ojb.broker.util.ObjectModificationDefaultImpl;

class DBChanges {
    private PersistenceBroker broker;
    private Set newObjs = null;
    private Set objsToStore = null;
    private Set objsToDelete = null;
    private Map<RelationTupleInfo,RelationTupleInfo> mToNTuples = null;

    public boolean needsWrite() {
	return ((newObjs != null) && (! newObjs.isEmpty()))
	    || ((objsToStore != null) && (! objsToStore.isEmpty()))
	    || ((objsToDelete != null) && (! objsToDelete.isEmpty()))
	    || ((mToNTuples != null) && (! mToNTuples.isEmpty()));
    }

    public PersistenceBroker getOJBBroker() {
	if (broker == null) {
	    broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	}
	return broker;
    }


    public void storeNewObject(Object obj) {
	if (newObjs == null) {
	    newObjs = new HashSet();
	}
	newObjs.add(obj);
	if (objsToDelete != null) {
	    objsToDelete.remove(obj);
	}
    }

    public void storeObject(Object obj) {
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

    void makePersistent(int txNumber) {
	PersistenceBroker pb = null;

	try {
	    pb = getOJBBroker();
	    pb.beginTransaction();

	    // store new objects
	    if (newObjs != null) {
		for (Object obj : newObjs) {
		    pb.store(obj, ObjectModificationDefaultImpl.INSERT);
		}
	    }

	    // update objects
	    if (objsToStore != null) {
		for (Object obj : objsToStore) {
		    //pb.serviceBrokerHelper().link(obj, true);
		    pb.store(obj, ObjectModificationDefaultImpl.UPDATE);
		}
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
	    //Connection conn = pb.serviceConnectionManager().getConnection();
	    
	    
	    pb.commitTransaction();
	    pb.close();
	    pb = null;
	} catch (PersistenceBrokerException e) {
	    e.printStackTrace();
	    if (pb != null) {
		pb.abortTransaction();
		pb.close();
		pb = null;
	    }
	    // this shouldn't happen
	    throw new Error(e);
	} finally {
	    if (pb != null) {
		pb.close();
	    }
	    broker = null;
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
	String relation;
	Object obj1;
	String colNameOnObj1;
	Object obj2;
	String colNameOnObj2;
	boolean remove;

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
}
