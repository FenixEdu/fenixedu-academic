package net.sourceforge.fenixedu.stm;

import org.apache.ojb.broker.PersistenceBroker;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.cache.FenixCache;

public abstract class Transaction extends jvstm.Transaction {

    private static final FenixCache cache = new FenixCache();
    private static boolean initialized = false;

    static {
	jvstm.Transaction.setTransactionFactory(new jvstm.TransactionFactory() {
		public jvstm.Transaction makeTopLevelTransaction(int txNumber) {
		    return new TopLevelTransaction(txNumber);
		}
	    });
    }


    private Transaction() {
 	// this is never to be used!!!
 	super(0);
    }

    static synchronized void initializeIfNeeded() {
	if (! initialized) {
	    int maxTx = TransactionChangeLogs.initializeTransactionSystem();
	    if (maxTx >= 0) {
		System.out.println("Setting the last committed TX number to " + maxTx);
		setCommitted(maxTx);
	    } else {
		throw new Error("Couldn't determine the last transaction number");
	    }
	    initialized = true;
	}
    }


    public static jvstm.Transaction begin() {
	initializeIfNeeded();	
	return jvstm.Transaction.begin();
    }

    public static FenixTransaction currentFenixTransaction() {
	return (FenixTransaction)current();
    }

    protected static DBChanges currentDBChanges() {
	return currentFenixTransaction().getDBChanges();
    }

    public static void logAttrChange(DomainObject obj, String attrName) {
	currentDBChanges().logAttrChange(obj, attrName);
    }

    public static void storeNewObject(DomainObject obj) {
	currentDBChanges().storeNewObject(obj);
    }

    public static void storeObject(DomainObject obj, String attrName) {
	currentDBChanges().storeObject(obj, attrName);
    }

    public static void deleteObject(Object obj) {
	currentDBChanges().deleteObject(obj);
    }

    public static void addRelationTuple(String relation, Object obj1, String colNameOnObj1, Object obj2, String colNameOnObj2) {
	currentDBChanges().addRelationTuple(relation, obj1, colNameOnObj1, obj2, colNameOnObj2);
    }

    public static void removeRelationTuple(String relation, Object obj1, String colNameOnObj1, Object obj2, String colNameOnObj2) {
	currentDBChanges().removeRelationTuple(relation, obj1, colNameOnObj1, obj2, colNameOnObj2);
    }

    public static PersistenceBroker getOJBBroker() {
	return currentDBChanges().getOJBBroker();
    }

    public static FenixCache getCache() {
	return cache;
    }


    // This is just for debug... It should be removed later
    static {
	Thread monitorThread = new Thread() {
		int previousSize = 0;

		public void run() {
		    while (true) {
			synchronized (ACTIVE_TXS) {
			    while (ACTIVE_TXS.getQueueSize() == previousSize) {
				try {
				    ACTIVE_TXS.wait();
				} catch (InterruptedException ie) {
				    return;
				}
			    }

			    previousSize = ACTIVE_TXS.getQueueSize();
			    //System.out.println("Monitoring queue.  Size = " + previousSize);
			    if (previousSize > 30) {
				TopLevelTransaction oldestTx = (TopLevelTransaction)ACTIVE_TXS.getOldestTx();
				System.out.println("WARNING: more than " + previousSize + " Txs in queue to be finished... : " + oldestTx.getNumber());
				oldestTx.logServiceInfo();
			    }
			}
		    }
		}
	    };
	monitorThread.setDaemon(true);
	monitorThread.start();
    }
}
