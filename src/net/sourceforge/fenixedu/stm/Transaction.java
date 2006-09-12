package net.sourceforge.fenixedu.stm;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.cache.FenixCache;

import org.apache.ojb.broker.PersistenceBroker;

public abstract class Transaction extends jvstm.Transaction {

    private final static int WARN_TX_QUEUE_SIZE_LIMIT = 1000;
    private final static int TRANSACTION_MAX_DURATION_MILLIS = 100 * 1000;

    public final static TransactionStatistics STATISTICS = new TransactionStatistics();

    static {
        new StatisticsThread().start();
    }

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
        if (ACTIVE_TXS.getQueueSize() > WARN_TX_QUEUE_SIZE_LIMIT) {
            System.out.printf("WARNING: the number of active transactions is %d\n", ACTIVE_TXS.getQueueSize());
            System.out.printf("    The oldest active transaction is %s\n", ACTIVE_TXS.getOldestTx());
        }
	jvstm.Transaction tx = jvstm.Transaction.begin();
        tx.setTimeoutMillis(TRANSACTION_MAX_DURATION_MILLIS);
        return tx;
    }

    public static void forceFinish() {
	if (current() != null) {
	    try {
		commit();
	    } catch (Throwable t) {
		abort();
	    }
	}
    }

    public static void abort() {
        STATISTICS.incAborts();

        jvstm.Transaction.abort();
        Transaction.begin();
        Transaction.currentFenixTransaction().setReadOnly();
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

    public static void withTransaction(jvstm.TransactionalCommand command) {
        while (true) {
            Transaction.begin();
            boolean txFinished = false;
            try {
                command.doIt();
                Transaction.commit();
                txFinished = true;
                return;
            } catch (jvstm.CommitException ce) {
                System.out.println("Restarting TX because of a conflict");
                Transaction.abort();
                txFinished = true;
            } finally {
                if (! txFinished) {
                    Transaction.abort();
                }
            }
        }
    }
}
