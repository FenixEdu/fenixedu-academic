package net.sourceforge.fenixedu.stm;

import net.sourceforge.fenixedu.domain.DomainObject;

import org.apache.ojb.broker.Identity;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.accesslayer.LookupException;
import org.apache.ojb.broker.metadata.ClassDescriptor;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;



class TransactionChangeLogs {

    private static class OIDInfo {
	final Class realClass;
	final Class topLevelClass;
	
	OIDInfo(Class realClass, Class topLevelClass) {
	    this.realClass = realClass;
	    this.topLevelClass = topLevelClass;
	}
    }

    private static HashMap<String,OIDInfo> CLASS_OIDS = new HashMap<String,OIDInfo>();

    static Identity getObjectIdentity(PersistenceBroker pb, String className, int idInternal) {
	OIDInfo info = CLASS_OIDS.get(className);
	if (info == null) {
	    try {
		Class realClass = Class.forName(className);
		ClassDescriptor cld = pb.getClassDescriptor(realClass);
		Class topLevelClass = pb.getTopLevelClass(realClass);
		info = new OIDInfo(realClass, topLevelClass);
		CLASS_OIDS.put(className, info);
	    } catch (ClassNotFoundException cnfe) {
		throw new Error("Couldn't find class " + className + ": " + cnfe);
	    }
	}

	return new Identity(info.realClass, info.topLevelClass, new Object[] { new Integer(idInternal) });
    }



    // ------------------------------------------------------------

    private static class ChangeLog {
	final Identity oid;
	final String attr;

	ChangeLog(Identity oid, String attr) {
	    this.oid = oid;
	    this.attr = attr;
	}
    }

    private static class ChangeLogSet {
	final int txNumber;
	final List<ChangeLog> changeLogs;

	ChangeLogSet(int txNumber) {
	    this.txNumber = txNumber;
	    this.changeLogs = new ArrayList<ChangeLog>();
	}
    }

    private static ConcurrentLinkedQueue<ChangeLogSet> CHANGE_LOGS = new ConcurrentLinkedQueue<ChangeLogSet>();

    public static void cleanOldLogs(int txNumber) {
	while ((! CHANGE_LOGS.isEmpty()) && (CHANGE_LOGS.peek().txNumber <= txNumber)) {
	    CHANGE_LOGS.poll();
	}
    }

    private static boolean registerChangeLogSet(PersistenceBroker pb, ChangeLogSet clSet) {
	CHANGE_LOGS.add(clSet);

	// invalidate cache
	boolean updatedCache = false;

	for (ChangeLog log : clSet.changeLogs) {
	    DomainObject existingObject = (DomainObject)pb.serviceObjectCache().lookup(log.oid);
	    if (existingObject != null) {
		updatedCache = true;
		existingObject.invalidate(log.attr, clSet.txNumber);
	    }
	}

	return updatedCache;
    }

    public static synchronized boolean updateFromTxLogsOnDatabase(PersistenceBroker pb) throws SQLException,LookupException {
	Connection conn = pb.serviceConnectionManager().getConnection();
	Statement stmt = conn.createStatement();

	// read tx logs
	int maxTxNumber = Transaction.getCommitted();
	ResultSet rs = stmt.executeQuery("SELECT OBJ_CLASS,OBJ_ID,OBJ_ATTR,TX_NUMBER FROM TX_CHANGE_LOGS WHERE TX_NUMBER > " 
					 + maxTxNumber 
					 + " ORDER BY TX_NUMBER");

	int previousTxNum = -1;
	ChangeLogSet clSet = null;
	boolean updatedCache = false;

	while (rs.next()) {
	    int txNum = rs.getInt(4);

	    if (txNum != previousTxNum) {
		if (clSet != null) {
		    updatedCache |= registerChangeLogSet(pb, clSet);
		}
		maxTxNumber = Math.max(maxTxNumber, txNum);
		clSet = new ChangeLogSet(txNum);
		previousTxNum = txNum;
	    }

	    String className = rs.getString(1);
	    int idInternal = rs.getInt(2);
	    String attr = rs.getString(3);

	    clSet.changeLogs.add(new ChangeLog(getObjectIdentity(pb, className, idInternal), attr));
	}

	// add last built ChangeLogSet, if any
	if (clSet != null) {
	    updatedCache |= registerChangeLogSet(pb, clSet);
	}

	Transaction.setCommitted(maxTxNumber);

	return updatedCache;
    }

    public static int findNewerVersionFor(Object obj, String attr, int txNumber) {
	Class objClass = obj.getClass();
	for (ChangeLogSet clSet : CHANGE_LOGS) {
	    if (clSet.txNumber > txNumber) {
		for (ChangeLog log : clSet.changeLogs) {
		    if ((log.oid.getObjectsRealClass() == objClass) && log.attr.equals(attr)) {
			return clSet.txNumber;
		    }
		}
	    }
	}
	return txNumber;
    }
}
