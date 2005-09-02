package net.sourceforge.fenixedu.stm;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.ojb.broker.PersistenceBroker;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.cache.FenixCache;

public abstract class Transaction extends jvstm.Transaction {

    private static final Queue<FenixTransaction> ACTIVE_TXS = new PriorityBlockingQueue<FenixTransaction>();
    private static final FenixCache cache = new FenixCache();
    private static boolean initialized = false;


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


    static void addToQueue(FenixTransaction tx) {
	ACTIVE_TXS.offer(tx);
    }
    
    static void removeFromQueue(FenixTransaction tx) {
	ACTIVE_TXS.remove(tx);
    }


    public static jvstm.Transaction begin() {
        return begin(-1);
    }

    protected static jvstm.Transaction begin(int txNumber) {
	initializeIfNeeded();

        jvstm.Transaction parent = current.get();
        TopLevelTransaction tx = null;
        if (parent == null) {
            tx = new TopLevelTransaction(txNumber);
        } else {
            //tx = new NestedTransaction(parent);
	    throw new Error("Nested transactions not supported yet...");
        }        
        current.set(tx);
	addToQueue(tx);
        return tx;
    }

    public static void abort() {
	FenixTransaction tx = currentFenixTransaction();
	try {
	    jvstm.Transaction.abort();
	} finally {
	    noteTxFinished(tx);
	}
    }

    public static int commit() {
	FenixTransaction tx = currentFenixTransaction();
	int result = jvstm.Transaction.commit();
	noteTxFinished(tx);
	return result;
    }

    private static void noteTxFinished(FenixTransaction tx) {
	tx.finish();
	synchronized (ACTIVE_TXS) {
	    boolean needsClean = false;
	    if (ACTIVE_TXS.size() > 30) {
		System.out.println("WARNING: more than " + ACTIVE_TXS.size() + " Txs in queue to be finished... : " + ACTIVE_TXS.peek().getNumber());
	    }
	    FenixTransaction oldestTx = ACTIVE_TXS.peek();
	    while ((oldestTx != null) && oldestTx.isFinished()) {
		needsClean = true;
		ACTIVE_TXS.poll();
		oldestTx = ACTIVE_TXS.peek();
	    }
	    if (needsClean) {
		TransactionChangeLogs.cleanOldLogs((oldestTx == null) ? getCommitted() : oldestTx.getNumber());
	    }
	}
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
	return currentFenixTransaction().getOJBBroker();
    }

    public static FenixCache getCache() {
	return cache;
    }

    public static int findVersionFor(Object obj, String attr, int txNumber) {
	if (txNumber < getCommitted()) {
	    return TransactionChangeLogs.findNewerVersionFor(obj, attr, txNumber);
	} else {
	    return txNumber;
	}
    }
}
