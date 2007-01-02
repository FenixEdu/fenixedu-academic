package net.sourceforge.fenixedu.stm;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.cache.FenixCache;

import org.apache.ojb.broker.PersistenceBroker;

public abstract class Transaction extends jvstm.Transaction {

    private static int WARN_TX_QUEUE_SIZE_LIMIT = 1000;
    private static int TRANSACTION_MAX_DURATION_MILLIS = 600 * 1000;

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

                public jvstm.Transaction makeReadOnlyTopLevelTransaction(int txNumber) {
		    return new ReadOnlyTopLevelTransaction(txNumber);
                }
	    });
    }


    private Transaction() {
 	// this is never to be used!!!
 	super(0);
    }

    public static void setScriptMode() {
        WARN_TX_QUEUE_SIZE_LIMIT = -1;
        TRANSACTION_MAX_DURATION_MILLIS = -1;
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
        return Transaction.begin(false);
    }

    public static jvstm.Transaction begin(boolean readOnly) {
	initializeIfNeeded();	
        if ((WARN_TX_QUEUE_SIZE_LIMIT > 0) && (ACTIVE_TXS.getQueueSize() > WARN_TX_QUEUE_SIZE_LIMIT)) {
            System.out.printf("WARNING: the number of active transactions is %d\n", ACTIVE_TXS.getQueueSize());
            final jvstm.Transaction transaction = ACTIVE_TXS.getOldestTx();
            System.out.printf("    The oldest active transaction is %s\n", transaction);
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("### Begin #########################################");
            for (final StackTraceElement stackTraceElement : transaction.getThread().getStackTrace()) {
                stringBuilder.append("\n");
                stringBuilder.append(stackTraceElement.getClassName());
                stringBuilder.append(" : ");
                stringBuilder.append(stackTraceElement.getMethodName());
                stringBuilder.append(" : ");
                stringBuilder.append(stackTraceElement.getLineNumber());
            }
            System.out.println(stringBuilder.toString());
            stringBuilder.append("\n--- End -------------------------------------------");
        }
	jvstm.Transaction tx = jvstm.Transaction.begin(readOnly);
        if (TRANSACTION_MAX_DURATION_MILLIS > 0) {
            tx.setTimeoutMillis(TRANSACTION_MAX_DURATION_MILLIS);
        }
        return tx;
    }

    public static void forceFinish() {
	if (current() != null) {
	    try {
		commit();
	    } catch (Throwable t) {
		System.out.println("Aborting from Transaction.forceFinish(). If being called from CloseTransactionFilter it will leave an open transaction.");
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
        withTransaction(false, command);
    }

    public static void withTransaction(boolean readOnly, jvstm.TransactionalCommand command) {
        while (true) {
            Transaction.begin(readOnly);
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
                    STATISTICS.incAborts();
                    jvstm.Transaction.abort();
                }
            }
        }
    }
}
