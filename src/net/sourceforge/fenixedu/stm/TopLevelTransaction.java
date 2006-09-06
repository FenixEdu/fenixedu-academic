package net.sourceforge.fenixedu.stm;

import java.sql.SQLException;
import java.util.ArrayList;

import jvstm.CommitException;
import jvstm.VBoxBody;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.accesslayer.LookupException;

public class TopLevelTransaction extends jvstm.TopLevelTransaction implements FenixTransaction {

    private static final ArrayList<CommitListener> COMMIT_LISTENERS = new ArrayList<CommitListener>();

    public static void addCommitListener(CommitListener listener) {
        synchronized (COMMIT_LISTENERS) {
            COMMIT_LISTENERS.add(listener);
        }
    }

    public static void removeCommitListener(CommitListener listener) {
        synchronized (COMMIT_LISTENERS) {
            COMMIT_LISTENERS.remove(listener);
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
    private DBChanges dbChanges = new DBChanges();

    private ServiceInfo serviceInfo = ServiceInfo.getCurrentServiceInfo();

    private PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();

    private Thread executingThread = Thread.currentThread();

    TopLevelTransaction(int number) {
        super(number);

        // open a connection to the database and set this tx number to the
        // number that
        // corresponds to that connection number. The connection number should
        // always be
        // greater than the current number, because the current number is
        // obtained from
        // Transaction.getCommitted, which is set only after the commit to the
        // database
        setNumber(updateFromTxLogsOnDatabase(number));
    }

    public PersistenceBroker getOJBBroker() {
        return broker;
    }

    public void setReadOnly() {
        // a null dbChanges indicates a read-only tx
        this.dbChanges = null;
    }

    protected Transaction makeNestedTransaction() {
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
        StackTraceElement[] txStack = executingThread.getStackTrace();
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
        notifyBeforeCommit(this);
        super.doCommit();
        notifyAfterCommit(this);
    }

    public ReadSet getReadSet() {
        return new ReadSet(bodiesRead);
    }

    protected <T> jvstm.VBoxBody<T> getBodyForWrite(jvstm.VBox<T> vbox) {
        if (dbChanges == null) {
            throw new IllegalWriteException();
        } else {
            return super.getBodyForWrite(vbox);
        }
    }

    protected <T> void setPerTxValue(jvstm.PerTxBox<T> box, T value) {
        if (dbChanges == null) {
            throw new IllegalWriteException();
        } else {
            super.setPerTxValue(box, value);
        }
    }

    public <T> VBoxBody<T> getBodyForRead(VBox<T> vbox, Object obj, String attr) {
        VBoxBody<T> body = getBodyInTx(vbox);

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
        return body;
    }

    public <T> VBoxBody<T> getBodyInTx(VBox<T> vbox) {
        VBoxBody<T> body = getBodyWritten(vbox);
        if (body == null) {
            body = getBodyRead(vbox);
        }
        return body;
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

            System.out.println(
                      ",1: " + (time1 == 0 || time2 == 0 ? "" : (time2 - time1))
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

    protected void doCommit(int newTxNumber) {
        try {
            dbChanges.makePersistent(getOJBBroker(), newTxNumber);
        } catch (SQLException sqle) {
            throw new Error("Error while accessing database", sqle);
        } catch (LookupException le) {
            throw new Error("Error while obtaining database connection", le);
        }
        dbChanges.cache();
        super.doCommit(newTxNumber);
    }
}
