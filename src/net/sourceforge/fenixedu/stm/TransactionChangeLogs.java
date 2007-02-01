package net.sourceforge.fenixedu.stm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import jvstm.VBoxBody;
import net.sourceforge.fenixedu.domain.DomainObject;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.accesslayer.LookupException;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.util.ClassHelper;

public class TransactionChangeLogs {

    private static final ReentrantLock PROCESS_ALIEN_TX_LOCK = new ReentrantLock(true);

    private static class ClassInfo {
	final ClassDescriptor classDescriptor;
	final Class topLevelClass;
	
	ClassInfo(ClassDescriptor classDescriptor, Class topLevelClass) {
	    this.classDescriptor = classDescriptor;
	    this.topLevelClass = topLevelClass;
	}
    }

    private static Map<String,ClassInfo> CLASS_INFOS = new ConcurrentHashMap<String,ClassInfo>();

    static DomainObject getDomainObject(PersistenceBroker pb, String className, int idInternal) {
	ClassInfo info = CLASS_INFOS.get(className);
	if (info == null) {
	    try {
		Class realClass = Class.forName(className);
		ClassDescriptor cld = pb.getClassDescriptor(realClass);
		Class topLevelClass = pb.getTopLevelClass(realClass);
		info = new ClassInfo(cld, topLevelClass);
		CLASS_INFOS.put(className, info);
	    } catch (ClassNotFoundException cnfe) {
		throw new Error("Couldn't find class " + className + ": " + cnfe);
	    }
	}

        DomainObject obj = (DomainObject)Transaction.getCache().lookup(info.topLevelClass, idInternal);

        if (obj == null) {
            obj = (DomainObject)ClassHelper.buildNewObjectInstance(info.classDescriptor);
            obj.setIdInternal(idInternal);
                
            // cache object
            obj = (DomainObject)Transaction.getCache().cache(obj);
        }

        return obj;
    }



    // ------------------------------------------------------------

    private static class AlienTransaction {
	final int txNumber;

        // the set of objects is kept so that a strong reference exists 
        // for each of the objects modified by another server until no running 
        // transaction in the current VM may need to access it
	private Map<DomainObject,List<String>> objectAttrChanges = new HashMap<DomainObject,List<String>>();

        private List<VBoxBody> newBodies = new ArrayList<VBoxBody>();

	AlienTransaction(int txNumber) {
	    this.txNumber = txNumber;
	}

        void register(DomainObject obj, String attrName) {
            List<String> allAttrs = objectAttrChanges.get(obj);

            if (allAttrs == null) {
                allAttrs = new LinkedList<String>();
                objectAttrChanges.put(obj, allAttrs);
            }

            allAttrs.add(attrName);
        }

        void commit() {
            for (Map.Entry<DomainObject,List<String>> entry : objectAttrChanges.entrySet()) {
                DomainObject obj = entry.getKey();
                List<String> allAttrs = entry.getValue();

                for (String attr : allAttrs) {
                    newBodies.add(obj.addNewVersion(attr, txNumber));
                }
            }
        }

        void freeResources() {
            // remove reference to the previous body, making it GCable
	    for (VBoxBody body : newBodies) {
                // the bodies may be null in some cases: see the 
                // comment on the VBox.addNewVersion method
                if (body != null) {
                    body.clearPrevious();
                }
	    }
	    
            newBodies = null;
            objectAttrChanges = null;
        }
    }


    // Alien transactions not yet GCed
    private static final ConcurrentLinkedQueue<AlienTransaction> ALIEN_TRANSACTIONS = new ConcurrentLinkedQueue<AlienTransaction>();

    static {
	Transaction.addTxQueueListener(new jvstm.TxQueueListener() {
		public void noteOldestTransaction(int previousOldest, int newOldest) {
		    if (previousOldest != newOldest) {
			cleanOldAlienTxs(newOldest);
		    }
		}
	    });
    }

    public static void cleanOldAlienTxs(int txNumber) {
        while ((! ALIEN_TRANSACTIONS.isEmpty()) && (ALIEN_TRANSACTIONS.peek().txNumber <= txNumber)) {
            ALIEN_TRANSACTIONS.poll().freeResources();
        }
    }


    public static int updateFromTxLogsOnDatabase(PersistenceBroker pb, int txNumber) throws SQLException,LookupException {
	return updateFromTxLogsOnDatabase(pb, txNumber, false);
    }

    public static int updateFromTxLogsOnDatabase(PersistenceBroker pb, int txNumber, boolean forUpdate) throws SQLException,LookupException {
	Connection conn = pb.serviceConnectionManager().getConnection();

	// ensure that the connection is up-to-date
	conn.commit();
        
	Statement stmt = conn.createStatement();
        
	// read tx logs
	int maxTxNumber = txNumber;

	ResultSet rs = stmt.executeQuery("SELECT OBJ_CLASS,OBJ_ID,OBJ_ATTR,TX_NUMBER FROM TX_CHANGE_LOGS WHERE TX_NUMBER > " 
					 + maxTxNumber 
					 + " ORDER BY TX_NUMBER"
					 + (forUpdate ? " FOR UPDATE" : ""));

        // if there are any results to be processed, process them
	if (rs.next()) {
            maxTxNumber = processAlienTransaction(pb, rs);
	}

	return maxTxNumber;
    }

    private static int processAlienTransaction(PersistenceBroker pb, ResultSet rs) throws SQLException {
        // Acquire an exclusion lock to process the result set.
        // Even though the processing of alien transactions could be made concurrently,
        // there is no point in doing so, as they would be repeating their work
        // therefore, at least for machines with a small number of processors, it 
        // is preferable to let just one thread work its way through the alien records.
        // Then, the concurrent threads can skip through the already processed records
        PROCESS_ALIEN_TX_LOCK.lock();

        try {
            // Here, after acquiring the lock, we know that no new transactions can start, because
            // all transactions must call the updateFromTxLogsOnDatabase method with a number which 
            // is necessarily less than the number we are processing, and, therefore, will have to
            // come into this method, blocking in the lock.
            // Likewise for a commit of a write transaction.
            
            int currentCommittedNumber = Transaction.getCommitted();

            int txNum = rs.getInt(4);

            // skip all the records already processed
            while ((txNum <= currentCommittedNumber) && rs.next()) {
                txNum = rs.getInt(4);
            }

            if (txNum <= currentCommittedNumber) {
                // the records ended, so simply get out of here, with the higher number we got
                return txNum;
            }
            
            // now, it's time to process the new changeLog records

            AlienTransaction alienTx = new AlienTransaction(txNum);

            while (alienTx != null) {
                String className = rs.getString(1);
                int idInternal = rs.getInt(2);
                String attr = rs.getString(3);

                DomainObject obj = getDomainObject(pb, className, idInternal);
                alienTx.register(obj, attr);

                int nextTxNum = -1;
                if (rs.next()) {
                    nextTxNum = rs.getInt(4);
                }

                if (nextTxNum != txNum) {
                    // finished the records for an alien transaction, so "commit" it
                    alienTx.commit();

                    // add it to the queue of alienTxs to be GCed later
                    ALIEN_TRANSACTIONS.offer(alienTx);

                    Transaction.setCommitted(txNum);

                    if (nextTxNum != -1) {
                        // there are more to process, create a new alien transaction
                        txNum = nextTxNum;
                        alienTx = new AlienTransaction(txNum);
                    } else {
                        // finish the loop
                        alienTx = null;
                    }
                }
            }

            return txNum;
        } finally {
            PROCESS_ALIEN_TX_LOCK.unlock();
        }
    }
    

    public static int initializeTransactionSystem() {
	// find the last committed transaction
	PersistenceBroker broker = null;

	try {
	    broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	    broker.beginTransaction();

	    Connection conn = broker.serviceConnectionManager().getConnection();
	    Statement stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT MAX(TX_NUMBER) FROM TX_CHANGE_LOGS");
	    int maxTx = (rs.next() ? rs.getInt(1) : -1);

	    broker.commitTransaction();

	    new CleanThread().start();

	    return maxTx;
	} catch (Exception e) {
	    throw new Error("Couldn't initialize the transaction system");
	} finally {
	    if (broker != null) {
		broker.close();
	    }
	}
    }

    private static class CleanThread extends Thread {
	private static final long SECONDS_BETWEEN_UPDATES = 120;

	private String server;
	private int lastTxNumber = -1;
	
	CleanThread() {
            this.server = Util.getServerName();

	    setDaemon(true);
	}
	
        public void run() {
	    while (lastTxNumber == -1) {
		initializeServerRecord();
		try {
		    sleep(SECONDS_BETWEEN_UPDATES * 1000);
		} catch (InterruptedException ie) {
		    // ignore it
		}
	    }
	    
	    while (true) {
		updateServerRecord();
		try {
		    sleep(SECONDS_BETWEEN_UPDATES * 1000);
		} catch (InterruptedException ie) {
		    // ignore it
		}
	    }
	}
	
	private void initializeServerRecord() {
	    int currentTxNumber = Transaction.getCommitted();

	    PersistenceBroker broker = null;
	    
	    try {
		broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		broker.beginTransaction();
		
		Connection conn = broker.serviceConnectionManager().getConnection();
		Statement stmt = conn.createStatement();

		// delete previous record for this server and insert a new one
		stmt.executeUpdate("DELETE FROM LAST_TX_PROCESSED WHERE SERVER = '" + server + "' or LAST_UPDATE < ADDDATE(NOW(),-1)");
		stmt.executeUpdate("INSERT INTO LAST_TX_PROCESSED VALUES ('" + server + "'," + currentTxNumber + ",null)");
		
		broker.commitTransaction();

		this.lastTxNumber = currentTxNumber;
	    } catch (Exception e) {
		e.printStackTrace();
		System.out.println("Couldn't initialize the clean thread");
		//throw new Error("Couldn't initialize the clean thread");
	    } finally {
		if (broker != null) {
		    broker.close();
		}
	    }
	}
	
	private void updateServerRecord() {
	    int currentTxNumber = Transaction.getCommitted();

	    PersistenceBroker broker = null;
	    
	    try {
		broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		broker.beginTransaction();
		
		Connection conn = broker.serviceConnectionManager().getConnection();
		Statement stmt = conn.createStatement();

		// update record for this server
		stmt.executeUpdate("UPDATE LAST_TX_PROCESSED SET LAST_TX=" 
				   + currentTxNumber 
				   + ",LAST_UPDATE=NULL WHERE SERVER = '" 
				   + server + "'");
		
		// delete obsolete values
		ResultSet rs = stmt.executeQuery("SELECT MIN(LAST_TX) FROM LAST_TX_PROCESSED WHERE LAST_UPDATE > NOW() - " 
						 + (2 * SECONDS_BETWEEN_UPDATES));
		int min = (rs.next() ? rs.getInt(1) : 0);
		if (min > 0) {
		    stmt.executeUpdate("DELETE FROM TX_CHANGE_LOGS WHERE TX_NUMBER < " + min);
		}

		broker.commitTransaction();

		this.lastTxNumber = currentTxNumber;
	    } catch (Exception e) {
		e.printStackTrace();
		System.out.println("Couldn't update database in the clean thread");
		//throw new Error("Couldn't update database in the clean thread");
	    } finally {
		if (broker != null) {
		    broker.close();
		}
	    }
	}
    }
}
