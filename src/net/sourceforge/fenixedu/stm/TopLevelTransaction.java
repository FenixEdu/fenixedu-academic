package net.sourceforge.fenixedu.stm;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;

import jvstm.CommitException;
import jvstm.VBoxBody;
import jvstm.cps.ConsistencyCheckTransaction;
import jvstm.cps.ConsistentTopLevelTransaction;
import jvstm.cps.DependenceRecord;
import jvstm.util.Cons;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.accesslayer.LookupException;

import net.sourceforge.fenixedu.domain.DomainObject;

public class TopLevelTransaction extends ConsistentTopLevelTransaction implements FenixTransaction {

    private static final Object COMMIT_LISTENERS_LOCK = new Object();
    private static volatile Cons<CommitListener> COMMIT_LISTENERS = Cons.empty();

    public static void addCommitListener(CommitListener listener) {
        synchronized (COMMIT_LISTENERS_LOCK) {
            COMMIT_LISTENERS = COMMIT_LISTENERS.cons(listener);
        }
    }

    public static void removeCommitListener(CommitListener listener) {
        synchronized (COMMIT_LISTENERS_LOCK) {
            COMMIT_LISTENERS = COMMIT_LISTENERS.removeFirst(listener);
        }
    }

    private static void notifyBeforeCommit(TopLevelTransaction tx) {
        for (CommitListener cl : COMMIT_LISTENERS) {
            cl.beforeCommit(tx);
        }
    }

    private static void notifyAfterCommit(TopLevelTransaction tx) {
        for (CommitListener cl : COMMIT_LISTENERS) {
            cl.afterCommit(tx);
        }
    }

    // Each TopLevelTx has its DBChanges
    // If this slot is changed to null, it is an indication that the
    // transaction does not allow more changes
    private DBChanges dbChanges = null;

    private ServiceInfo serviceInfo = ServiceInfo.getCurrentServiceInfo();

    private PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();

    TopLevelTransaction(int number) {
        super(number);

	// open a connection to the database and set this tx number to the number that
	// corresponds to that connection number.  The connection number should always be 
	// greater than the current number, because the current number is obtained from
	// Transaction.getCommitted, which is set only after the commit to the database
	setNumber(updateFromTxLogsOnDatabase(number));
        initDbChanges();
    }

    protected void initDbChanges() {
        this.dbChanges = new DBChanges();
    }

    public PersistenceBroker getOJBBroker() {
        return broker;
    }

    public DomainObject getDomainObject(String classname, int oid) {
        return TransactionChangeLogs.getDomainObject(broker, classname, oid);
    }

    public void setReadOnly() {
        // a null dbChanges indicates a read-only tx
        this.dbChanges = null;
    }

    public Transaction makeNestedTransaction() {
        throw new Error("Nested transactions not supported yet...");
    }

    private int updateFromTxLogsOnDatabase(int currentNumber) {
        try {
            return TransactionChangeLogs.updateFromTxLogsOnDatabase(getOJBBroker(), currentNumber);
        } catch (Exception sqle) {
//            sqle.printStackTrace();
            throw new Error("Error while updating from TX_CHANGE_LOGS: Cannot proceed: " + sqle.getMessage(), sqle);
        }
    }

    public void logServiceInfo() {
        System.out.println("Transaction " + this + " created for service: " + serviceInfo.serviceName
                + ", username = " + serviceInfo.username
        // + ", args = " + serviceInfo.getArgumentsAsString()
                );
        System.out.println("Currently executing:");
        StackTraceElement[] txStack = getThread().getStackTrace();
        for (int i = 0; (i < txStack.length) && (i < 5); i++) {
            System.out.println("-----> " + txStack[i]);
        }
    }

    protected void finish() {
        super.finish();
        if (broker != null) {
            broker.close();
            broker = null;
        }
        dbChanges = null;
    }

    protected void doCommit() {
        if (isWriteTransaction()) {
            Transaction.STATISTICS.incWrites();
        } else {
            Transaction.STATISTICS.incReads();
        }

        notifyBeforeCommit(this);
        super.doCommit();
        notifyAfterCommit(this);
    }

    protected boolean validateCommit() {
        boolean result = super.validateCommit();

        if (! result) {
            Transaction.STATISTICS.incConflicts();
        }

        return result;
    }

    public ReadSet getReadSet() {
        return new ReadSet(bodiesRead);
    }

    public <T> void setBoxValue(jvstm.VBox<T> vbox, T value) {
        if (dbChanges == null) {
            throw new IllegalWriteException();
        } else {
            super.setBoxValue(vbox, value);
        }
    }

    public <T> void setPerTxValue(jvstm.PerTxBox<T> box, T value) {
        if (dbChanges == null) {
            throw new IllegalWriteException();
        } else {
            super.setPerTxValue(box, value);
        }
    }

    public <T> T getBoxValue(VBox<T> vbox, Object obj, String attr) {
        T value = getLocalValue(vbox);

        if (value == null) {
            // no local value for the box
            VBoxBody<T> body = getBodyRead(vbox);

            if (body == null) {
                body = vbox.body.getBody(number);
                if (body.value == VBox.NOT_LOADED_VALUE) {
                    vbox.reload(obj, attr);
                    // after the reload, the same body should have a new value
                    // if not, then something gone wrong and its better to abort
                    if (body.value == VBox.NOT_LOADED_VALUE) {
                        System.out.println("Couldn't load the attribute " + attr + " for class "
                                           + obj.getClass());
                        throw new VersionNotAvailableException();
                    }
                }

                bodiesRead.put(vbox, body);
            }

            value = body.value;
        }

        return (value == NULL_VALUE) ? null : value;
    }

    public boolean isBoxValueLoaded(VBox vbox) {
        Object localValue = getLocalValue(vbox);

        if (localValue == VBox.NOT_LOADED_VALUE)  {
            return false;
        }

        if (localValue != null)  {
            return true;
        }

        VBoxBody body = vbox.body.getBody(number);
        return (body.value != VBox.NOT_LOADED_VALUE);
    }

    public DBChanges getDBChanges() {
        if (dbChanges == null) {
            // if it is null, it means that the transaction is a read-only
            // transaction
            throw new IllegalWriteException();
        } else {
            return dbChanges;
        }
    }

    protected boolean isWriteTransaction() {
        return ((dbChanges != null) && dbChanges.needsWrite()) || super.isWriteTransaction();
    }

    protected int performValidCommit() {
        // in memory everything is ok, but we need to check against the db
        PersistenceBroker pb = getOJBBroker();

        int currentPriority = Thread.currentThread().getPriority();
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        try {

        long time1 = System.currentTimeMillis();
        long time2 = 0;
        long time3 = 0;
        long time4 = 0;
        long time5 = 0;
        long time6 = 0;
        long time7 = 0;
        long time8 = 0;
        long time9 = 0;

        try {
            if (!pb.isInTransaction()) {
                pb.beginTransaction();
            }
            time2 = System.currentTimeMillis();
            int txNumber = getNumber();
            try {
                time3 = System.currentTimeMillis();
                // the updateFromTxLogs is made with the txNumber minus 1 to ensure that the select
                // for update will return at least a record, and, therefore, lock the record
                // otherwise, the mysql server may allow the select for update to continue
                // concurrently with other executing commits in other servers
                if (TransactionChangeLogs.updateFromTxLogsOnDatabase(pb, txNumber - 1, true) != txNumber) {
                    //if (TransactionChangeLogs.updateFromTxLogsOnDatabase(pb, txNumber, true) != txNumber) {
                    // the cache may have been updated, so perform the
                    // tx-validation again
                    time4 = System.currentTimeMillis();
                    if (!validateCommit()) {
                        System.out.println("Invalid commit. Restarting.");
                        throw new jvstm.CommitException();
                    }
                    time5 = System.currentTimeMillis();
                }
            } catch (SQLException sqlex) {
                System.out.println("SqlException: " + sqlex.getMessage());
                throw new CommitException();
            } catch (LookupException le) {
                throw new Error("Error while obtaining database connection", le);
            }

            time6 = System.currentTimeMillis();
            txNumber = super.performValidCommit();
            time7 = System.currentTimeMillis();
            // ensure that changes are visible to other TXs before releasing
            // lock
            try {
                pb.commitTransaction();
                time8 = System.currentTimeMillis();
            } catch (Throwable t) {
                t.printStackTrace();
                System.out.println("Error while commiting exception. Terminating server.");
                System.err.flush();
                System.out.flush();
                System.exit(-1);
            }
            pb = null;
            return txNumber;
        } finally {
            if (pb != null) {
                pb.abortTransaction();
                time9 = System.currentTimeMillis();
            }

            if ((time8 - time1) > 500) {
                System.out.println(
                                   "performValidCommit: ,1: " + (time1 == 0 || time2 == 0 ? "" : (time2 - time1))
                                   + "   ,2: " + (time2 == 0 || time3 == 0 ? "" : (time3 - time2))
                                   + "   ,3: " + (time3 == 0 || time4 == 0 ? "" : (time4 - time3))
                                   + "   ,4: " + (time4 == 0 || time5 == 0 ? "" : (time5 - time4))
                                   + "   ,5: " + (time5 == 0 || time6 == 0 ? "" : (time6 - time5))
                                   + "   ,6: " + (time6 == 0 || time7 == 0 ? "" : (time7 - time6))
                                   + "   ,7: " + (time7 == 0 || time8 == 0 ? "" : (time8 - time7))
                                   + "   ,8: " + (time8 == 0 || time9 == 0 ? "" : (time9 - time8))
                                   );
            }
        }
        } finally {
            Thread.currentThread().setPriority(currentPriority);
        }
    }

    protected void doCommit(int newTxNumber) {
        long time1 = System.currentTimeMillis();
        long time2 = 0;
        long time3 = 0;
        long time4 = 0;
        long time5 = 0;
        long time6 = 0;

        try {
            time2 = System.currentTimeMillis();
            dbChanges.makePersistent(getOJBBroker(), newTxNumber);
            time3 = System.currentTimeMillis();
        } catch (SQLException sqle) {
            throw new Error("Error while accessing database", sqle);
        } catch (LookupException le) {
            throw new Error("Error while obtaining database connection", le);
        }
        time4 = System.currentTimeMillis();
        dbChanges.cache();
        time5 = System.currentTimeMillis();
        super.doCommit(newTxNumber);
        time6 = System.currentTimeMillis();

        if ((time6 - time1) > 500) {
            System.out.println(
                               "doCommit: ,1: " + (time1 == 0 || time2 == 0 ? "" : (time2 - time1))
                               + "   ,2: " + (time2 == 0 || time3 == 0 ? "" : (time3 - time2))
                               + "   ,3: " + (time3 == 0 || time4 == 0 ? "" : (time4 - time3))
                               + "   ,4: " + (time4 == 0 || time5 == 0 ? "" : (time5 - time4))
                               + "   ,5: " + (time5 == 0 || time6 == 0 ? "" : (time6 - time5))
                               );
        }
    }


    // consistency-predicates-system methods

    @Override
    protected void checkConsistencyPredicates() {
        // check all the consistency predicates for the objects modified in this transaction
        for (Object obj : getDBChanges().getModifiedObjects()) {
            checkConsistencyPredicates(obj);
	}

        super.checkConsistencyPredicates();
    }

    @Override
    protected void checkConsistencyPredicates(Object obj) {
        if (getDBChanges().isDeleted(obj)) {
            // don't check deleted objects
            return;
        } else {
            super.checkConsistencyPredicates(obj);
        }
    }

    @Override
    protected ConsistencyCheckTransaction makeConsistencyCheckTransaction(Object obj) {
        return new FenixConsistencyCheckTransaction(this, obj);
    }

    @Override
    protected Iterator<DependenceRecord> getDependenceRecordsToRecheck() {
        // for now, just return an empty iterator
        return Util.emptyIterator();
    }
}
